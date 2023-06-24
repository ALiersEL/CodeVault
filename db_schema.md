user(<u>user_id</u>, user_name, password_hash, role, date_registered, phone_number, email, avatar, description, last_login, last_modified, is_email_verified)
```PgSQL
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role SMALLINT NOT NULL DEFAULT 1,
    date_registered TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    phone_number VARCHAR(20),
    email VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    description TEXT,
    last_login TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE
);
```

folder(<u>folder_id</u>, folder_name, folder_path, parent_folder_id, date_added, last_modified, user_id)
```PgSQL
CREATE TABLE folder (
    folder_id SERIAL PRIMARY KEY,
    folder_name VARCHAR(50) NOT NULL,
    folder_path TEXT NOT NULL,
    parent_folder_id INTEGER DEFAULT NULL,
    date_added TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE,
    FOREIGN KEY (parent_folder_id) REFERENCES folder(folder_id)
    ON DELETE CASCADE
);
```

problem(<u>problem_id</u>, problem_title, problem_content, problem_type, difficulty, status, mastery, date_added, last_modified, user_id, folder_id)
```PgSQL
CREATE TABLE problem (
    problem_id SERIAL PRIMARY KEY,
    problem_title VARCHAR(255) NOT NULL,
    problem_content TEXT NOT NULL,
    problem_type SMALLINT,
    difficulty SMALLINT,
    status BOOLEAN NOT NULL DEFAULT FALSE,
    mastery SMALLINT NOT NULL DEFAULT 0,
    date_added TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id INTEGER NOT NULL,
    folder_id INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE,
    FOREIGN KEY (folder_id) REFERENCES folder(folder_id)
    ON DELETE CASCADE
);
```


company(<u>company_id</u>, company_name, user_id)
```PgSQL
CREATE TABLE company (
    company_id SERIAL PRIMARY KEY,
    company_name VARCHAR(50) NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE
);
```


department(<u>department_id</u>, department_name, company_id)
```PgSQL
CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL,
    company_id INTEGER NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
    ON DELETE CASCADE
);
```

post(<u>post_id</u>, post_name, department_id)
```PgSQL
CREATE TABLE post (
    post_id SERIAL PRIMARY KEY,
    post_name VARCHAR(50) NOT NULL,
    department_id INTEGER NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
    ON DELETE CASCADE
);
```

problem_company(<u>problem_id</u>, <u>company_id</u>)
```PgSQL
CREATE TABLE problem_company (
    problem_id INTEGER NOT NULL,
    company_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id) 
    ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES company(company_id) 
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, company_id)
);
```

problem_department(<u>problem_id</u>, <u>department_id</u>)
```PgSQL
CREATE TABLE problem_department (
    problem_id INTEGER NOT NULL,
    department_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, department_id)
);
```

problem_post(<u>problem_id</u>, <u>post_id</u>)
```PgSQL
CREATE TABLE problem_post (
    problem_id INTEGER NOT NULL,
    post_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, post_id)
);
```

category(<u>category_id</u>, category_name, user_id)
```PgSQL
CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE
);
```

problem_category(<u>problem_id</u>, <u>category_id</u>)
```PgSQL
CREATE TABLE problem_category (
    problem_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, category_id)
);
```


note(<u>note_id</u>, note_text, last_modified, problem_id)
```PgSQL
CREATE TABLE note (
    note_id SERIAL PRIMARY KEY,
    note_text TEXT NOT NULL,
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    problem_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE
);
```

code(<u>code_id</u>, code_text, code_language, last_modified, problem_id)
```PgSQL
CREATE TABLE code (
    code_id SERIAL PRIMARY KEY,
    code_text TEXT NOT NULL,
    code_language SMALLINT NOT NULL,
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    problem_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE
);
```