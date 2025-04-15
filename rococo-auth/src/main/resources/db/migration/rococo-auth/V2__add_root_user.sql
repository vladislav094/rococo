create extension if not exists "uuid-ossp";
create extension if not exists pgcrypto;

DO $$
    DECLARE
        v_user_id UUID;
    BEGIN
        -- Вставляем пользователя
        INSERT INTO "user" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
        VALUES (
                   'root',
                   '{bcrypt}' || crypt('1234', gen_salt('bf', 10)),
                   true, true, true, true
               )
        ON CONFLICT (username) DO NOTHING
        RETURNING id INTO v_user_id;

        -- Если пользователь уже существует
        IF v_user_id IS NULL THEN
            SELECT id INTO v_user_id FROM "user" WHERE username = 'root';
        END IF;

        -- Чистый вариант добавления authorities без неоднозначности
        INSERT INTO "authority" (user_id, authority)
        SELECT v_user_id, 'read'
        WHERE NOT EXISTS (
            SELECT 1 FROM "authority"
            WHERE user_id = v_user_id AND authority = 'read'
        );

        INSERT INTO "authority" (user_id, authority)
        SELECT v_user_id, 'write'
        WHERE NOT EXISTS (
            SELECT 1 FROM "authority"
            WHERE user_id = v_user_id AND authority = 'write'
        );
    END $$;