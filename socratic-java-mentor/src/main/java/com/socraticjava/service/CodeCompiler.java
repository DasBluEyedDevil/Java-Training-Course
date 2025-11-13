package com.socraticjava.service;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.*;

/**
 * Service for compiling student Java code in-memory
 * Uses the Java Compiler API to compile code without writing to disk
 */
public class CodeCompiler {

    /**
     * Compilation result containing success status, errors, and compiled class
     */
    public static class CompilationResult {
        private final boolean success;
        private final String errors;
        private final Map<String, byte[]> compiledClasses;

        public CompilationResult(boolean success, String errors, Map<String, byte[]> compiledClasses) {
            this.success = success;
            this.errors = errors;
            this.compiledClasses = compiledClasses;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrors() {
            return errors;
        }

        public Map<String, byte[]> getCompiledClasses() {
            return compiledClasses;
        }
    }

    /**
     * Compiles Java source code in-memory
     * @param className The name of the class (must match class name in source)
     * @param sourceCode The Java source code
     * @return CompilationResult with success status and any errors
     */
    public CompilationResult compile(String className, String sourceCode) {
        try {
            // Get the Java compiler
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                return new CompilationResult(false,
                    "Java compiler not available. Make sure you're running with a JDK, not just a JRE.",
                    null);
            }

            // Prepare in-memory file manager
            InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(null, null, null)
            );

            // Prepare source file
            JavaFileObject sourceFile = new InMemoryJavaFile(className, sourceCode);

            // Capture compilation errors
            StringWriter errorWriter = new StringWriter();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

            // Compile
            JavaCompiler.CompilationTask task = compiler.getTask(
                errorWriter,
                fileManager,
                diagnostics,
                null, // No compiler options
                null, // No annotation processing
                Collections.singletonList(sourceFile)
            );

            boolean success = task.call();

            if (success) {
                return new CompilationResult(true, "", fileManager.getCompiledClasses());
            } else {
                // Format error messages
                StringBuilder errors = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    errors.append("Line ").append(diagnostic.getLineNumber())
                          .append(": ").append(diagnostic.getMessage(null))
                          .append("\n");
                }
                return new CompilationResult(false, errors.toString(), null);
            }

        } catch (Exception e) {
            return new CompilationResult(false, "Compilation error: " + e.getMessage(), null);
        }
    }

    /**
     * Forwarding file manager base class
     */
    private static class ForwardingStandardJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        protected ForwardingStandardJavaFileManager(StandardJavaFileManager fileManager) {
            super(fileManager);
        }
    }

    /**
     * In-memory representation of a Java source file
     */
    private static class InMemoryJavaFile extends SimpleJavaFileObject {
        private final String sourceCode;

        protected InMemoryJavaFile(String className, String sourceCode) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension),
                  Kind.SOURCE);
            this.sourceCode = sourceCode;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
        }
    }

    /**
     * Custom file manager that stores compiled classes in memory
     */
    private static class InMemoryFileManager extends ForwardingStandardJavaFileManager {
        private final Map<String, byte[]> compiledClasses = new HashMap<>();

        protected InMemoryFileManager(StandardJavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                                                   JavaFileObject.Kind kind, FileObject sibling) {
            return new InMemoryClassFile(className);
        }

        public Map<String, byte[]> getCompiledClasses() {
            return compiledClasses;
        }

        /**
         * In-memory representation of a compiled class file
         */
        private class InMemoryClassFile extends SimpleJavaFileObject {
            private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            private final String className;

            protected InMemoryClassFile(String className) {
                super(URI.create("bytes:///" + className.replace('.', '/') + Kind.CLASS.extension),
                      Kind.CLASS);
                this.className = className;
            }

            @Override
            public OutputStream openOutputStream() {
                return outputStream;
            }

            @Override
            public byte[] toByteArray() {
                byte[] bytes = outputStream.toByteArray();
                compiledClasses.put(className, bytes);
                return bytes;
            }
        }
    }
}
