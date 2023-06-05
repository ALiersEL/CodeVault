CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role SMALLINT NOT NULL DEFAULT 1,
    date_registered TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    phone_number VARCHAR(20),
    email VARCHAR(50),
    ac_easy INTEGER NOT NULL DEFAULT 0,
    ac_medium INTEGER NOT NULL DEFAULT 0,
    ac_hard INTEGER NOT NULL DEFAULT 0,
    total_easy INTEGER NOT NULL DEFAULT 0,
    total_medium INTEGER NOT NULL DEFAULT 0,
    total_hard INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE folder (
    folder_id SERIAL PRIMARY KEY,
    folder_name VARCHAR(50) NOT NULL,
    parent_folder_id INTEGER DEFAULT NULL,
    date_added TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE,
    FOREIGN KEY (parent_folder_id) REFERENCES folder(folder_id)
    ON DELETE CASCADE
);

CREATE TABLE company (
    company_id SERIAL PRIMARY KEY,
    company_name VARCHAR(50) NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE
);

CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL,
    company_id INTEGER NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
    ON DELETE CASCADE
);

CREATE TABLE post (
    post_id SERIAL PRIMARY KEY,
    post_name VARCHAR(50) NOT NULL,
    department_id INTEGER NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
    ON DELETE CASCADE
);

CREATE TABLE problem (
    problem_id SERIAL PRIMARY KEY,
    problem_title VARCHAR(100) NOT NULL,
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


CREATE TABLE problem_company (
    problem_id INTEGER NOT NULL,
    company_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id) 
    ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES company(company_id) 
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, company_id)
);

CREATE TABLE problem_department (
    problem_id INTEGER NOT NULL,
    department_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id) 
    ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id) 
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, department_id)
);

CREATE TABLE problem_post (
    problem_id INTEGER NOT NULL,
    post_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id) 
    ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id) 
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, post_id)
);

CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE
);

CREATE TABLE problem_category (
    problem_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, category_id)
);

CREATE TABLE note (
    note_id SERIAL PRIMARY KEY,
    note_text TEXT NOT NULL,
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    problem_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE
);

CREATE TABLE code (
    code_id SERIAL PRIMARY KEY,
    code_text TEXT NOT NULL,
    code_language SMALLINT NOT NULL,
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    problem_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE
);


-- 新增用户， 用触发器自动添加该用户的根目录
CREATE OR REPLACE FUNCTION add_root_folder()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO folder (folder_name, user_id)
    VALUES ('root', NEW.user_id);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER add_root_folder_trigger
AFTER INSERT ON "user"
FOR EACH ROW
EXECUTE FUNCTION add_root_folder();


