create table PERSON
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar     not null,
    created_at TIMESTAMPTZ not null DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ not null DEFAULT CURRENT_TIMESTAMP
);

create table School
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar     not null,
    created_at TIMESTAMPTZ not null DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ not null DEFAULT CURRENT_TIMESTAMP
);

create table Course
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar     not null,
    created_at TIMESTAMPTZ not null DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ not null DEFAULT CURRENT_TIMESTAMP
);
