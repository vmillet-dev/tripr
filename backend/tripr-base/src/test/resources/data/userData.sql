INSERT INTO "user" (
    id,
    email,
    password,
    role_id
) VALUES (
    1000,
    'admin@invalid.bootify.io',
    '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
    1200
);

INSERT INTO "user" (
    id,
    email,
    password,
    role_id
) VALUES (
    1001,
    'member@invalid.bootify.io',
    '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
    1201
);
