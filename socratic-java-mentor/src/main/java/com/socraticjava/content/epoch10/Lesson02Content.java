package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 10, Lesson 2: Database Migrations with Flyway
 *
 * This lesson teaches professional database schema management using
 * Flyway migrations with Spring Boot 3.
 */
public class Lesson02Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Database Migrations with Flyway",
            """
            # Database Migrations with Flyway

            ## Why Database Migrations Matter

            Imagine you deploy a new version of your application, but the database schema
            doesn't match. Your app expects a `phone_number` column, but the production
            database doesn't have it. **The application crashes.**

            Or worse: Your teammate adds a column locally, but forgets to tell anyone.
            When you pull their code, your app breaks because your database is out of sync.

            These problems happen **constantly** without database migrations:
            - Developers have different database schemas
            - Staging and production are out of sync
            - No record of who changed what and when
            - Can't reproduce database state
            - Schema changes cause deployment failures
            - Rolling back code doesn't roll back schema

            **Database migrations solve all of these problems.**

            ## The Analogy: Building Renovations

            **Without migrations (manual SQL scripts):**
            - Foreman verbally tells workers what to build
            - No written record of changes
            - Each worker remembers different instructions
            - When inspector arrives, building doesn't match any plan
            - Can't prove building meets code requirements
            - If something breaks, unclear what was changed

            **With migrations (Flyway):**
            - Every change documented in numbered blueprints
            - V1: Foundation, V2: Walls, V3: Roof
            - Each worker executes same numbered plans in order
            - Building inspector can verify exactly what was done
            - Complete audit trail of every change
            - If issue found, easy to see what changed when

            Database migrations are **version control for your database schema**.

            ## Flyway Core Concepts

            ### What is Flyway?

            Flyway is a database migration tool that:
            - Versions your database schema
            - Applies migrations in order
            - Tracks which migrations have run
            - Ensures all environments have the same schema
            - Provides a complete audit history

            ### How Flyway Works

            1. **Migration Scripts**: SQL files with versioned names
            2. **Schema History Table**: Tracks which migrations ran
            3. **Automatic Execution**: Runs pending migrations on startup
            4. **Validation**: Ensures migration integrity

            ```
            Your Project
            └── src/main/resources/db/migration/
                ├── V1__create_users_table.sql
                ├── V2__add_email_index.sql
                ├── V3__create_tasks_table.sql
                └── V4__add_user_phone_number.sql

            Database: flyway_schema_history
            ┌─────────┬──────────────────────────┬─────────┬────────────────────┐
            │ version │ description              │ success │ installed_on       │
            ├─────────┼──────────────────────────┼─────────┼────────────────────┤
            │ 1       │ create users table       │ true    │ 2025-01-10 10:00   │
            │ 2       │ add email index          │ true    │ 2025-01-10 10:00   │
            │ 3       │ create tasks table       │ true    │ 2025-01-12 14:30   │
            │ 4       │ add user phone number    │ true    │ 2025-01-15 09:15   │
            └─────────┴──────────────────────────┴─────────┴────────────────────┘
            ```

            When your application starts:
            1. Flyway checks `flyway_schema_history` table
            2. Compares with migration files in your project
            3. Runs any migrations that haven't been applied yet
            4. Records successful migrations in history table

            ## Setting Up Flyway in Spring Boot 3

            ### Step 1: Add Dependency

            Add to `pom.xml`:

            ```xml
            <dependency>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-core</artifactId>
            </dependency>

            <!-- For PostgreSQL -->
            <dependency>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-database-postgresql</artifactId>
            </dependency>

            <!-- For MySQL -->
            <!--
            <dependency>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-mysql</artifactId>
            </dependency>
            -->
            ```

            **Important:** Spring Boot 3 auto-configures Flyway if it's on the classpath.
            No additional configuration beans needed!

            ### Step 2: Configure Application Properties

            Add to `application.yml`:

            ```yaml
            spring:
              datasource:
                url: jdbc:postgresql://localhost:5432/taskdb
                username: taskuser
                password: ${DB_PASSWORD}
              jpa:
                hibernate:
                  ddl-auto: validate  # IMPORTANT: Change from 'update' to 'validate'
                show-sql: false

              flyway:
                enabled: true
                locations: classpath:db/migration
                baseline-on-migrate: true
                baseline-version: 0
                validate-on-migrate: true
                out-of-order: false
                clean-disabled: true  # NEVER allow clean in production!
            ```

            **Critical Configuration:**
            - `hibernate.ddl-auto: validate` - Hibernate no longer creates tables!
            - `flyway.enabled: true` - Enable Flyway migrations
            - `flyway.locations` - Where migration scripts are located
            - `flyway.baseline-on-migrate: true` - Allow Flyway on existing database
            - `flyway.clean-disabled: true` - Prevent accidental data loss

            ### Step 3: Create Migration Directory

            ```
            src/main/resources/db/migration/
            ```

            This is where all your migration SQL files go.

            ## Writing Migration Scripts

            ### Naming Convention

            Flyway uses strict naming: `V{VERSION}__{DESCRIPTION}.sql`

            ```
            V1__create_users_table.sql
            V2__add_email_index.sql
            V3__create_tasks_table.sql
            V4__add_user_phone_number.sql
            V5__create_task_categories_table.sql
            ```

            **Rules:**
            - Must start with `V` (uppercase)
            - Version number (integers: 1, 2, 3 or decimals: 1.1, 1.2, 2.1)
            - Two underscores `__` separate version from description
            - Description uses underscores for spaces
            - Must end with `.sql`

            ### V1: Initial Schema

            `V1__create_users_table.sql`:

            ```sql
            CREATE TABLE users (
                id BIGSERIAL PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                name VARCHAR(100) NOT NULL,
                role VARCHAR(50) NOT NULL DEFAULT 'USER',
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            );

            CREATE INDEX idx_users_email ON users(email);

            -- Add some initial data if needed
            INSERT INTO users (email, password, name, role)
            VALUES ('admin@example.com', '$2a$10$...hashed...', 'Admin User', 'ADMIN');
            ```

            ### V2: Add New Table

            `V2__create_tasks_table.sql`:

            ```sql
            CREATE TABLE tasks (
                id BIGSERIAL PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                description TEXT,
                status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
                user_id BIGINT NOT NULL,
                due_date TIMESTAMP,
                completed_at TIMESTAMP,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_tasks_user FOREIGN KEY (user_id)
                    REFERENCES users(id) ON DELETE CASCADE
            );

            CREATE INDEX idx_tasks_user_id ON tasks(user_id);
            CREATE INDEX idx_tasks_status ON tasks(status);
            CREATE INDEX idx_tasks_due_date ON tasks(due_date);
            ```

            ### V3: Add Column to Existing Table

            `V3__add_user_phone_number.sql`:

            ```sql
            -- Add new column (nullable first for existing data)
            ALTER TABLE users
            ADD COLUMN phone_number VARCHAR(20);

            -- Add index for phone lookups
            CREATE INDEX idx_users_phone ON users(phone_number);

            -- Optionally, you can set it to NOT NULL after data migration
            -- UPDATE users SET phone_number = 'Unknown' WHERE phone_number IS NULL;
            -- ALTER TABLE users ALTER COLUMN phone_number SET NOT NULL;
            ```

            ### V4: Modify Existing Column

            `V4__increase_task_title_length.sql`:

            ```sql
            -- Increase title length from 200 to 500
            ALTER TABLE tasks
            ALTER COLUMN title TYPE VARCHAR(500);
            ```

            ### V5: Add Constraints

            `V5__add_email_validation.sql`:

            ```sql
            -- Add check constraint for email format
            ALTER TABLE users
            ADD CONSTRAINT chk_users_email_format
            CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$');
            ```

            ### V6: Create Index for Performance

            `V6__add_composite_index_tasks.sql`:

            ```sql
            -- Composite index for common query pattern
            CREATE INDEX idx_tasks_user_status
            ON tasks(user_id, status);

            -- Drop old separate indexes if composite covers them
            DROP INDEX IF EXISTS idx_tasks_status;
            ```

            ## Versioning Strategies

            ### Linear Versioning (Recommended)

            ```
            V1__initial_schema.sql
            V2__add_tasks.sql
            V3__add_phone.sql
            V4__modify_title.sql
            ```

            **Pros:** Simple, clear order
            **Cons:** Merge conflicts if multiple developers create V4

            ### Timestamp Versioning

            ```
            V20250115120000__create_users.sql
            V20250115143000__add_tasks.sql
            V20250116090000__add_phone.sql
            ```

            **Pros:** No version conflicts between developers
            **Cons:** Less readable, need to check timestamps

            ### Hybrid Versioning

            ```
            V1.0__initial_schema.sql
            V1.1__add_indexes.sql
            V2.0__add_tasks.sql
            V2.1__modify_tasks.sql
            V3.0__add_categories.sql
            ```

            **Pros:** Groups related changes, avoids conflicts
            **Cons:** Need to coordinate major versions

            ## Handling Rollbacks

            ### Important Limitation

            **Flyway Community Edition does NOT support automatic rollbacks.**

            If you need undo migrations, you must:
            1. Use Flyway Teams/Enterprise (paid), OR
            2. Write manual rollback scripts

            ### Manual Rollback Strategy

            Create corresponding "down" migration for each "up" migration:

            `V4__add_user_phone_number.sql` (up):
            ```sql
            ALTER TABLE users
            ADD COLUMN phone_number VARCHAR(20);
            ```

            `V5__rollback_user_phone_number.sql` (down):
            ```sql
            ALTER TABLE users
            DROP COLUMN phone_number;
            ```

            **Caution:** This is data-destructive! Dropping columns loses data.

            ### Better Approach: Backward-Compatible Migrations

            Instead of rolling back, write migrations that are backward-compatible:

            ```sql
            -- V4: Add optional phone number (nullable)
            ALTER TABLE users
            ADD COLUMN phone_number VARCHAR(20);

            -- If V4 has issues, application can run without phone_number
            -- V5 can remove it later if needed:
            -- ALTER TABLE users DROP COLUMN phone_number;
            ```

            ### Database Backups for Rollbacks

            **Production Best Practice:**

            ```bash
            # Before running migrations
            pg_dump -U taskuser taskdb > backup_before_v4_$(date +%Y%m%d_%H%M%S).sql

            # If migration causes problems
            psql -U taskuser taskdb < backup_before_v4_20250115_140000.sql
            ```

            Always backup before running migrations in production!

            ## Team Workflow Best Practices

            ### 1. Never Modify Applied Migrations

            ```
            ❌ WRONG:
            1. Deploy V1__create_users.sql
            2. Realize email should be longer
            3. Modify V1__create_users.sql ← DON'T DO THIS!

            ✅ RIGHT:
            1. Deploy V1__create_users.sql
            2. Realize email should be longer
            3. Create V2__increase_email_length.sql ← New migration!
            ```

            **Why:** Flyway tracks checksums. Modifying applied migrations causes validation errors.

            ### 2. Test Migrations Locally First

            ```bash
            # Clean local database
            docker-compose down -v
            docker-compose up -d

            # Application starts, Flyway runs migrations
            mvn spring-boot:run

            # Verify schema
            psql -U taskuser taskdb
            \\dt  # List tables
            \\d users  # Describe users table
            ```

            ### 3. Migration Review Process

            Before merging:
            - [ ] Migration tested locally
            - [ ] Migration tested on staging
            - [ ] Migration is idempotent (can run multiple times safely)
            - [ ] Migration doesn't lock tables for long periods
            - [ ] Backward compatibility considered
            - [ ] Database backup plan documented

            ### 4. Handling Merge Conflicts

            If two developers create V4 at the same time:

            ```
            Developer A:
            V4__add_phone.sql

            Developer B:
            V4__add_address.sql  ← Conflict!
            ```

            **Solution:**
            ```bash
            # Developer B renames their migration
            git mv V4__add_address.sql V5__add_address.sql
            ```

            ### 5. Large Data Migrations

            For migrations that modify lots of data:

            ```sql
            -- V7__populate_default_categories.sql

            -- Process in batches to avoid long locks
            DO $$
            DECLARE
                batch_size INT := 1000;
                processed INT := 0;
            BEGIN
                LOOP
                    UPDATE tasks
                    SET category_id = 1
                    WHERE category_id IS NULL
                      AND id IN (
                          SELECT id FROM tasks
                          WHERE category_id IS NULL
                          LIMIT batch_size
                      );

                    IF NOT FOUND THEN
                        EXIT;
                    END IF;

                    processed := processed + batch_size;
                    RAISE NOTICE 'Processed % rows', processed;

                    -- Small delay to avoid overwhelming database
                    PERFORM pg_sleep(0.1);
                END LOOP;
            END $$;
            ```

            ## Common Mistakes to Avoid

            ### ❌ Using `hibernate.ddl-auto=update` with Flyway

            **Wrong:**
            ```yaml
            spring:
              jpa:
                hibernate:
                  ddl-auto: update  # ← Conflicts with Flyway!
            ```

            **Why:** Hibernate and Flyway both try to manage schema. Chaos ensues.

            **Right:**
            ```yaml
            spring:
              jpa:
                hibernate:
                  ddl-auto: validate  # ← Hibernate validates only
            ```

            ### ❌ Naming Migrations Incorrectly

            ```
            ❌ v1_create_users.sql      (lowercase v)
            ❌ V1_create_users.sql      (only one underscore)
            ❌ V1__create-users.sql     (hyphen instead of underscore)
            ❌ create_users.sql         (no version)
            ✅ V1__create_users.sql     (correct!)
            ```

            ### ❌ Not Using Transactions

            **Wrong:**
            ```sql
            -- If second statement fails, first still executed!
            CREATE TABLE users (...);
            CREATE TABLE tasks (...);  -- ERROR HERE
            -- users table still exists!
            ```

            **Right:**
            ```sql
            -- Wrap in transaction
            BEGIN;

            CREATE TABLE users (...);
            CREATE TABLE tasks (...);

            COMMIT;
            ```

            Flyway automatically wraps each migration in a transaction (for databases that support it).

            ### ❌ Not Testing Migrations

            **Wrong:**
            1. Write migration
            2. Commit directly to main
            3. Deploy to production
            4. Migration fails, production down!

            **Right:**
            1. Write migration
            2. Test locally
            3. Test on staging
            4. Review with team
            5. Deploy to production with backup ready

            ### ❌ Long-Running Migrations in Production

            ```sql
            -- This can lock table for hours on large tables!
            ALTER TABLE tasks
            ADD COLUMN category VARCHAR(100) NOT NULL DEFAULT 'General';
            ```

            **Better approach:**
            ```sql
            -- V8: Add column (nullable, fast)
            ALTER TABLE tasks ADD COLUMN category VARCHAR(100);

            -- V9: Populate data in batches (can run during low traffic)
            UPDATE tasks SET category = 'General' WHERE category IS NULL;

            -- V10: Add NOT NULL constraint (fast once data populated)
            ALTER TABLE tasks ALTER COLUMN category SET NOT NULL;
            ```

            ## Monitoring and Troubleshooting

            ### Check Migration Status

            ```bash
            # Connect to database
            psql -U taskuser taskdb

            # View migration history
            SELECT version, description, type, installed_on, success
            FROM flyway_schema_history
            ORDER BY installed_rank;
            ```

            ### Fix Failed Migration

            If migration fails:

            ```sql
            -- Check history
            SELECT * FROM flyway_schema_history WHERE success = false;

            -- Remove failed migration record
            DELETE FROM flyway_schema_history WHERE version = '4';

            -- Fix the migration script
            -- Then restart application to retry
            ```

            ### Baseline Existing Database

            If adding Flyway to existing project:

            ```yaml
            spring:
              flyway:
                baseline-on-migrate: true
                baseline-version: 0
            ```

            This creates migration history starting from version 0, treating existing schema as baseline.

            ## Production Deployment Checklist

            Before deploying migrations to production:

            - [ ] Tested locally with clean database
            - [ ] Tested on staging environment
            - [ ] Verified migration is backward-compatible
            - [ ] Database backup created
            - [ ] Migration execution time estimated
            - [ ] Rollback plan documented
            - [ ] Team notified of deployment
            - [ ] Monitoring alerts configured
            - [ ] Off-peak deployment time scheduled
            - [ ] Dry-run performed in production-like environment

            ## Summary

            Flyway provides professional database schema management:

            **Core Concepts:**
            - Version-controlled SQL migration files
            - Automatic tracking in flyway_schema_history table
            - Migrations run in order, once per environment
            - Complete audit trail of all schema changes

            **Setup:**
            1. Add flyway-core dependency
            2. Configure in application.yml
            3. Change hibernate.ddl-auto to validate
            4. Create db/migration directory
            5. Write versioned SQL migration files

            **Naming Convention:**
            - `V{VERSION}__{DESCRIPTION}.sql`
            - Example: `V1__create_users_table.sql`

            **Best Practices:**
            - Never modify applied migrations
            - Write backward-compatible migrations when possible
            - Test thoroughly before production
            - Always backup before production migrations
            - Use transactions for safety
            - Process large data changes in batches
            - Document rollback procedures

            **Rollback Strategy:**
            - Flyway Community doesn't support automatic rollback
            - Write new migrations to undo changes
            - Keep database backups for emergencies
            - Design migrations to be backward-compatible

            Flyway transforms database schema management from chaotic manual SQL scripts
            into a professional, version-controlled, auditable process.
            """,
            50  // Estimated minutes to complete
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        QuizQuestion question = new QuizQuestion(
            "Your teammate deployed a Flyway migration (V5__add_column.sql) to staging yesterday. Today you realized the column name is wrong. What should you do?",
            "C",
            """
            Understanding that applied Flyway migrations are immutable is fundamental to
            using Flyway correctly. This question tests whether you understand the core
            principle: once a migration runs, it's permanent history.

            The correct answer is C. Create a new migration (V6__rename_column.sql) to fix
            the mistake. This is the only safe and correct approach when using Flyway.

            **Why this is the right approach:**

            Flyway maintains a schema history table that tracks:
            - Which migrations have run
            - When they ran
            - Checksums of the migration files

            When Flyway starts, it:
            1. Computes checksum of each migration file
            2. Compares with checksums in flyway_schema_history
            3. If checksums don't match → validation error!

            If you modify V5__add_column.sql after it's been applied:
            ```
            Original V5:  ALTER TABLE users ADD COLUMN phone VARCHAR(20);
            Checksum: abc123

            Modified V5:  ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);
            Checksum: def456  ← DIFFERENT!

            Flyway sees: "V5 checksum is def456, but history says abc123"
            Result: VALIDATION ERROR - Application won't start!
            ```

            **The correct migration sequence:**

            `V5__add_column.sql` (already applied to staging):
            ```sql
            ALTER TABLE users ADD COLUMN phone VARCHAR(20);
            ```

            `V6__rename_column.sql` (new migration):
            ```sql
            ALTER TABLE users RENAME COLUMN phone TO phone_number;
            ```

            This approach:
            - Preserves accurate history (V5 really did create wrong column)
            - Works across all environments (local, staging, production)
            - Provides audit trail of the fix
            - No validation errors

            Why other answers are wrong:

            A is catastrophically wrong because modifying applied migrations breaks Flyway's
            validation. Here's what happens:

            Staging (V5 already applied):
            ```
            flyway_schema_history: V5, checksum=abc123
            ```

            You modify V5 locally:
            ```
            V5__add_column.sql: ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);
            New checksum: def456
            ```

            Next time you deploy or a colleague pulls your code:
            ```
            Error: Migration checksum mismatch for V5
            Expected: abc123
            Found:    def456

            APPLICATION STARTUP FAILS!
            ```

            To "fix" this, you'd have to:
            - Manually update checksum in flyway_schema_history (bad practice)
            - Or use `flyway repair` command (not available in startup, manual process)
            - And do this in EVERY environment (local, staging, production)

            This is error-prone, defeats Flyway's validation purpose, and creates confusion
            about what actually happened.

            B is wrong for multiple reasons:

            1. **You can't delete from schema history without consequences:**
            ```sql
            DELETE FROM flyway_schema_history WHERE version = '5';
            ```
            Now Flyway thinks V5 never ran, but the `phone` column still exists in staging!
            If you try to run modified V5, you'll get: "Column 'phone_number' already exists"

            2. **It doesn't work across environments:**
            Production might get V5 before you make this change. Then production has the
            wrong column, and you've lost the ability to track/fix it consistently.

            3. **It destroys audit trail:**
            The schema history table is your record of what happened. Deleting entries
            makes it impossible to understand the database's evolution.

            D is wrong because changing `baseline-version` doesn't solve this problem.
            Baselining is for:
            - Adding Flyway to an existing database
            - Treating current schema as "version 0"

            Setting `baseline-version: 6` wouldn't:
            - Undo V5
            - Allow modifying V5
            - Fix the wrong column name

            It would just confuse Flyway about which migrations to run.

            **Real-world scenario:**

            ```
            Timeline:
            Day 1: Developer A creates V5__add_column.sql with wrong name
            Day 1: V5 deployed to staging
            Day 2: Developer B realizes mistake
            Day 2: Developer B creates V6__rename_column.sql
            Day 2: V6 tested locally, deployed to staging
            Day 3: V5 and V6 deployed to production together

            Result:
            - All environments end up in same state
            - Complete history preserved
            - No validation errors
            - Clear audit trail of mistake and fix
            ```

            **Key principle:** Treat applied migrations as immutable history. You can't
            change the past, but you can add new migrations to fix mistakes.

            **Exception:** If migration NEVER reached any shared environment (only on your
            local machine), you can delete the file and start over. But once it's in staging,
            production, or another developer's environment—it's permanent.
            """
        );

        question.addChoice("A", "Modify V5__add_column.sql to use the correct column name since it's only in staging");
        question.addChoice("B", "Delete the V5 entry from flyway_schema_history table and modify the V5 migration file");
        question.addChoice("C", "Create a new migration V6__rename_column.sql to rename the column to the correct name");
        question.addChoice("D", "Set spring.flyway.baseline-version=6 to skip V5 and start fresh");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "You need to add a NOT NULL column to a table with 10 million rows in production. What is the BEST approach?",
            "D",
            """
            Large database migrations in production require careful planning to avoid
            extended downtime and table locks. This question tests understanding of
            migration strategies for production environments with significant data.

            The correct answer is D. Split into three migrations: add nullable column,
            populate data in batches, then add NOT NULL constraint. This approach
            minimizes lock time and allows the application to continue running.

            **Why this is the best approach:**

            **The Problem with Direct NOT NULL:**
            ```sql
            -- V10: Single migration (WRONG for large tables)
            ALTER TABLE tasks
            ADD COLUMN category VARCHAR(100) NOT NULL DEFAULT 'General';
            ```

            On a 10 million row table, this:
            - Locks the entire table while writing default value to every row
            - Can take minutes or even hours depending on table size
            - Blocks all reads and writes during this time
            - Application becomes unavailable
            - If it fails halfway, you're in trouble

            **The Three-Phase Approach:**

            **Phase 1 - Add Column (Fast, No Locks):**
            `V10__add_category_column.sql`:
            ```sql
            -- Adding nullable column is nearly instant
            ALTER TABLE tasks ADD COLUMN category VARCHAR(100);
            ```

            This takes milliseconds because:
            - PostgreSQL doesn't rewrite the table
            - No data is updated
            - No defaults applied
            - No locks held

            Application can continue running with category as optional field.

            **Phase 2 - Populate Data (Batched, Non-Blocking):**
            `V11__populate_category_data.sql`:
            ```sql
            -- Update in batches to avoid long locks
            DO $$
            DECLARE
                batch_size INT := 10000;
                total_updated BIGINT := 0;
                batch_count INT;
            BEGIN
                LOOP
                    -- Update one batch
                    WITH batch AS (
                        SELECT id FROM tasks
                        WHERE category IS NULL
                        LIMIT batch_size
                        FOR UPDATE SKIP LOCKED  -- Skip locked rows
                    )
                    UPDATE tasks
                    SET category = 'General'
                    FROM batch
                    WHERE tasks.id = batch.id;

                    GET DIAGNOSTICS batch_count = ROW_COUNT;

                    IF batch_count = 0 THEN
                        EXIT;
                    END IF;

                    total_updated := total_updated + batch_count;
                    RAISE NOTICE 'Updated % total rows', total_updated;

                    -- Small pause between batches
                    PERFORM pg_sleep(0.1);

                    -- Commit this batch
                    COMMIT;
                END LOOP;

                RAISE NOTICE 'Migration complete. Updated % total rows', total_updated;
            END $$;
            ```

            Benefits:
            - Updates 10,000 rows at a time
            - Commits after each batch
            - Short locks (milliseconds per batch)
            - Application can read/write between batches
            - If it fails, can restart from where it left off
            - SKIP LOCKED prevents blocking on concurrent updates

            **Phase 3 - Add Constraint (Fast):**
            `V12__add_category_not_null.sql`:
            ```sql
            -- Now that all rows have values, this is fast
            ALTER TABLE tasks ALTER COLUMN category SET NOT NULL;
            ```

            This is fast because:
            - All rows already have values
            - PostgreSQL just adds the constraint
            - Table scan happens but no writes
            - Takes seconds, not minutes

            **Timeline:**
            ```
            V10: Deploy (0.1 seconds) - Application runs with optional category
            V11: Runs automatically (5-10 minutes, non-blocking)
            V12: Deploy (5 seconds) - Category is now required

            Total blocking time: ~5 seconds
            Compare to single migration: 30+ minutes of downtime!
            ```

            Why other answers are wrong:

            A is wrong because disabling Flyway and doing manual SQL scripts defeats the
            entire purpose of using Flyway:
            - No version control of changes
            - No automatic tracking
            - Different developers might run different scripts
            - Staging and production diverge
            - No audit trail
            - Risk of forgetting to document changes

            Manual scripts might seem "easier" for tricky migrations, but they sacrifice
            all the benefits of version control and automation.

            B is wrong because while transactions are important, a single transaction for
            10 million rows causes exactly the problems we're trying to avoid:

            ```sql
            BEGIN;  -- Start transaction

            ALTER TABLE tasks
            ADD COLUMN category VARCHAR(100) NOT NULL DEFAULT 'General';
            -- This updates all 10 million rows in ONE transaction
            -- Holds exclusive lock for the entire duration
            -- 30+ minutes of application downtime

            COMMIT;  -- Finally release lock
            ```

            Transactions don't make long operations faster—they just make them atomic.
            For large data operations, you WANT multiple smaller transactions so locks
            are released between batches.

            C is wrong because dropping and recreating the table is extremely dangerous:

            ```sql
            -- Create new table with category column
            CREATE TABLE tasks_new (
                id BIGSERIAL PRIMARY KEY,
                title VARCHAR(500),
                category VARCHAR(100) NOT NULL DEFAULT 'General',
                ...
            );

            -- Copy data (this still takes 30+ minutes!)
            INSERT INTO tasks_new SELECT ... FROM tasks;

            -- Drop old table (LOSES ALL DATA if anything goes wrong!)
            DROP TABLE tasks;

            -- Rename new table
            ALTER TABLE tasks_new RENAME TO tasks;
            ```

            Problems:
            - Still takes a long time to copy 10 million rows
            - Application must be completely down during migration
            - If anything fails, old table is gone!
            - Foreign keys must be dropped and recreated
            - Indexes must be recreated
            - Permissions must be reapplied
            - High risk of data loss
            - No way to run application during migration

            This approach is only justified for:
            - Massive structural changes (like changing primary key type)
            - When you have planned maintenance windows
            - When you can afford extended downtime

            **Best Practices for Large Migrations:**

            1. **Test on production-size data** - Don't test on 100 rows, test on 10 million
            2. **Measure timing** - Know how long it will take
            3. **Use batching** - Break large operations into small chunks
            4. **Monitor progress** - Use RAISE NOTICE or logging
            5. **Handle failures** - Make migrations resumable
            6. **Consider deployment time** - Run during low-traffic periods
            7. **Have rollback plan** - Database backup ready
            8. **Communicate** - Alert team about long-running migrations

            **Additional advanced technique:**
            ```sql
            -- Add column with default (fast in PostgreSQL 11+)
            ALTER TABLE tasks
            ADD COLUMN category VARCHAR(100) DEFAULT 'General';

            -- Remove default (keeps existing values but new rows need explicit value)
            ALTER TABLE tasks
            ALTER COLUMN category DROP DEFAULT;

            -- Add NOT NULL
            ALTER TABLE tasks
            ALTER COLUMN category SET NOT NULL;
            ```

            PostgreSQL 11+ optimizes `ADD COLUMN ... DEFAULT` to avoid rewriting the table,
            making it fast even for large tables. But batched updates are still safer for
            complex data migrations.
            """
        );

        question.addChoice("A", "Disable Flyway temporarily and run manual SQL scripts directly on production");
        question.addChoice("B", "Create a single migration with the ALTER TABLE statement wrapped in a transaction");
        question.addChoice("C", "Drop the old table, create a new table with the column, and copy all data");
        question.addChoice("D", "Split into three migrations: add nullable column, populate data in batches, then add NOT NULL constraint");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "Two developers are working on different features. Developer A creates V8__add_tags.sql and Developer B creates V8__add_comments.sql. Both commit to their feature branches. What should happen when they try to merge?",
            "B",
            """
            This is a real-world scenario that happens frequently in team environments.
            Understanding how to resolve Flyway version conflicts is essential for
            collaborative development.

            The correct answer is B. The second developer to merge should rename their
            migration to the next available version number. This maintains Flyway's
            sequential versioning while allowing both features to be integrated.

            **The Scenario in Detail:**

            **Developer A (Feature: Tags):**
            ```
            git checkout -b feature/tags
            # Creates V8__add_tags.sql
            git add db/migration/V8__add_tags.sql
            git commit -m "Add tags feature"
            git push
            # Creates pull request, gets merged first
            ```

            **Developer B (Feature: Comments):**
            ```
            git checkout -b feature/comments
            # Creates V8__add_comments.sql (doesn't know about A's V8)
            git add db/migration/V8__add_comments.sql
            git commit -m "Add comments feature"
            git push
            # Creates pull request
            ```

            **When Developer B tries to merge:**
            ```
            main branch:
            └── db/migration/
                ├── V7__previous_migration.sql
                └── V8__add_tags.sql  ← Already exists!

            feature/comments branch:
            └── db/migration/
                ├── V7__previous_migration.sql
                └── V8__add_comments.sql  ← Conflict!
            ```

            **The Solution:**

            Developer B should:
            ```bash
            # 1. Pull latest main to see the conflict
            git pull origin main

            # 2. Rename their migration
            git mv db/migration/V8__add_comments.sql db/migration/V9__add_comments.sql

            # 3. Commit the rename
            git add db/migration/V9__add_comments.sql
            git commit -m "Rename migration to V9 to resolve conflict"

            # 4. Push and merge
            git push
            ```

            **Final result in main:**
            ```
            db/migration/
            ├── V7__previous_migration.sql
            ├── V8__add_tags.sql
            └── V9__add_comments.sql  ← Renamed
            ```

            **Why This Works:**

            All environments end up with consistent history:
            ```
            Developer A's local:
            1. V1-V7 already applied
            2. V8 (tags) runs locally
            3. Pushes V8

            Developer B's local:
            1. V1-V7 already applied
            2. V8 (comments - locally created) runs locally
            3. Pulls V8 (tags) from main
            4. Flyway sees local V8 checksum doesn't match
            5. Developer renames local migration to V9
            6. Deletes flyway_schema_history entry for their local V8
            7. Re-runs migrations: V8 (tags), V9 (comments)
            8. Now matches main

            Production:
            1. V1-V7 already applied
            2. Deployment runs V8 (tags), then V9 (comments)
            3. Everything works
            ```

            Why other answers are wrong:

            A is wrong and breaks Flyway fundamentally:

            ```
            V8a__add_tags.sql
            V8b__add_comments.sql
            ```

            Flyway doesn't support letter suffixes in version numbers. Version format is:
            - `V1`, `V2`, `V3` (integers)
            - `V1.1`, `V1.2`, `V2.1` (decimals)
            - NOT `V8a`, `V8b`

            If you try this:
            ```
            Error: Invalid version format "8a"
            Flyway expects numeric versions like 8 or 8.1
            ```

            Even if it worked, it would create confusion:
            - Which runs first, V8a or V8b?
            - What about V8c later?
            - Non-standard naming makes maintenance harder

            C is wrong because merging files doesn't make sense for SQL migrations:

            ```sql
            -- V8__combined.sql ???
            -- From tags feature:
            CREATE TABLE tags (...);

            -- From comments feature:
            CREATE TABLE comments (...);
            ```

            Problems with this approach:
            - Loses feature isolation (tags and comments are unrelated)
            - Creates tight coupling between independent features
            - Hard to rollback one feature without the other
            - Violates single responsibility principle
            - Makes code review harder (reviewing two features in one migration)
            - If one feature has issues, both are blocked

            Migrations should be atomic and feature-focused. Combining them is an anti-pattern.

            D is wrong because timestamps in version numbers create problems:

            ```
            V20250115120000__add_tags.sql
            V20250115143000__add_comments.sql
            ```

            While this prevents version conflicts (unlikely to have exact same timestamp),
            it has significant downsides:

            **Readability:**
            ```
            V8__add_tags.sql           ← Clear: this is the 8th migration
            V20250115120000__add_tags  ← Unclear: which order is this?
            ```

            **Git conflicts still possible:**
            ```
            Both developers:
            git checkout -b feature/...
            # Both create migration at 12:00:00

            V20250115120000__add_tags.sql
            V20250115120000__add_comments.sql  ← Still conflicts!
            ```

            **Maintenance burden:**
            - Need to check timestamp to find migrations
            - Hard to know how many migrations exist
            - Difficult to reference in documentation
            - Can't easily say "run migrations up to V8"

            **Better team workflow:**

            Many teams use this process to avoid conflicts:

            1. **Feature branch naming convention:**
            ```bash
            # Before creating migration, check latest version on main
            git fetch origin
            git show origin/main:db/migration  # See what's there

            # Reserve next version in team chat
            "I'm taking V8 for tags feature"
            ```

            2. **PR description template:**
            ```markdown
            ## Database Migrations
            - [ ] V8__add_tags.sql
            - [ ] Tested locally
            - [ ] Compatible with current production
            - [ ] No version conflicts with main
            ```

            3. **Automated checks:**
            ```yaml
            # .github/workflows/check-migrations.yml
            - name: Check for migration conflicts
              run: |
                # Fail if two files have same version number
                duplicates=$(ls db/migration/V* | sed 's/__.*$//' | sort | uniq -d)
                if [ ! -z "$duplicates" ]; then
                  echo "Duplicate migration versions found: $duplicates"
                  exit 1
                fi
            ```

            **Advanced: Decimal versioning to reduce conflicts:**
            ```
            Developer A: V8.1__add_tags.sql
            Developer B: V8.2__add_comments.sql
            ```

            This gives more "space" between versions, but still requires coordination.

            **Key principle:** Migrations are sequential history. When two developers
            create the same version, one must rename to maintain the sequence. This
            is a normal part of collaborative development with Flyway.
            """
        );

        question.addChoice("A", "Use version suffixes: V8a__add_tags.sql and V8b__add_comments.sql");
        question.addChoice("B", "The second developer to merge should rename their migration to V9");
        question.addChoice("C", "Merge both migration files into a single V8__combined.sql file");
        question.addChoice("D", "Switch to timestamp-based versioning (V20250115120000__...) to avoid conflicts");

        return question;
    }
}
