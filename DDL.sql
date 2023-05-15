CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    encrypted_password VARCHAR(100) NOT NULL,
    date_registered TIMESTAMP NOT NULL DEFAULT NOW(),
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
    date_added TIMESTAMP NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMP NOT NULL DEFAULT NOW(),
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
    date_added TIMESTAMP NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMP NOT NULL DEFAULT NOW(),
    company_id INTEGER,
    department_id INTEGER,
    post_id INTEGER,
    user_id INTEGER NOT NULL,
    folder_id INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
    ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES company(company_id),
    FOREIGN KEY (department_id) REFERENCES department(department_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    FOREIGN KEY (folder_id) REFERENCES folder(folder_id)
    ON DELETE CASCADE
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
    last_modified TIMESTAMP NOT NULL DEFAULT NOW(),
    problem_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE
);

CREATE TABLE code (
    code_id SERIAL PRIMARY KEY,
    code_text TEXT NOT NULL,
    code_language SMALLINT NOT NULL,
    last_modified TIMESTAMP NOT NULL DEFAULT NOW(),
    problem_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE
);

CREATE TABLE video (
    video_id SERIAL PRIMARY KEY,
    video_url VARCHAR(100) NOT NULL
);

CREATE TABLE problem_video (
    problem_id INTEGER NOT NULL,
    video_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (video_id) REFERENCES video(video_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, video_id)
);

CREATE TABLE note_video (
    note_id INTEGER NOT NULL,
    video_id INTEGER NOT NULL,
    FOREIGN KEY (note_id) REFERENCES note(note_id)
    ON DELETE CASCADE,
    FOREIGN KEY (video_id) REFERENCES video(video_id)
    ON DELETE CASCADE,
    PRIMARY KEY (note_id, video_id)
);

CREATE TABLE image (
    image_id SERIAL PRIMARY KEY,
    image_url VARCHAR(100) NOT NULL
);

CREATE TABLE problem_image (
    problem_id INTEGER NOT NULL,
    image_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES image(image_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, image_id)
);

CREATE TABLE note_image (
    note_id INTEGER NOT NULL,
    image_id INTEGER NOT NULL,
    FOREIGN KEY (note_id) REFERENCES note(note_id)
    ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES image(image_id)
    ON DELETE CASCADE,
    PRIMARY KEY (note_id, image_id)
);
