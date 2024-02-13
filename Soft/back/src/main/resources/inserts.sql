INSERT INTO t_role (id, name) VALUES
(1, 'MATCHMAKER'),
(2, 'BRIDE'),
(3, 'GROOM'),
(4, 'ENEMY'),
(5, 'ASSISTANT'),
(6, 'GUEST');

INSERT INTO permission (id, permission) VALUES
(1, 'hello:perm'),
(2, 'users:update'),
(3, 'users:readAll'),
(4, 'users:delete'),
(5, 'users:read:detailed');

INSERT INTO role_to_permission(role_id, permission_id) VALUES
(1, 1),
(1, 3),
(2, 2),
(3, 2),
(6, 2),
(2, 3),
(3, 3),
(4, 3),
(5, 3),
(6, 3),
(1, 4),
(2, 4),
(3, 4),
(4, 4),
(5, 4),
(6, 4),
(3, 5);
