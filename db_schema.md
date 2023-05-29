user(<u>user_id</u>, user_name, password_hash, role, date_registered, phone_number, email, ac_easy, ac_medium, ac_hard, total_easy, total_medium, total_hard)
```PgSQL
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role SMALLINT NOT NULL DEFAULT 1,
    date_registered TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    phone_number VARCHAR(20),
    email VARCHAR(50) NOT NULL,
    ac_easy INTEGER NOT NULL DEFAULT 0,
    ac_medium INTEGER NOT NULL DEFAULT 0,
    ac_hard INTEGER NOT NULL DEFAULT 0,
    total_easy INTEGER NOT NULL DEFAULT 0,
    total_medium INTEGER NOT NULL DEFAULT 0,
    total_hard INTEGER NOT NULL DEFAULT 0
);
```

folder(<u>folder_id</u>, folder_name, parent_folder_id, date_added, last_modified, user_id)
```PgSQL
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
```

problem(<u>problem_id</u>, problem_title, problem_content, problem_type, difficulty, status, mastery, date_added, last_modified, company_id, department_id, post_id, user_id, folder_id)
```PgSQL
CREATE TABLE problem (
    problem_id SERIAL PRIMARY KEY,
    problem_title VARCHAR(100) NOT NULL,
    problem_content JSONB NOT NULL,
    problem_type SMALLINT,
    difficulty SMALLINT,
    status BOOLEAN NOT NULL DEFAULT FALSE,
    mastery SMALLINT NOT NULL DEFAULT 0,
    date_added TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_modified TIMESTAMPTZ NOT NULL DEFAULT NOW(),
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

video(<u>video_id</u>, video_url)
```PgSQL
CREATE TABLE video (
    video_id SERIAL PRIMARY KEY,
    video_url VARCHAR(100) NOT NULL
);
```

problem_video(<u>problem_id</u>, <u>video_id</u>)
```PgSQL
CREATE TABLE problem_video (
    problem_id INTEGER NOT NULL,
    video_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (video_id) REFERENCES video(video_id)
    ON DELETE CASCADE
    PRIMARY KEY (problem_id, video_id)
);
```

note_video(<u>note_id</u>, <u>video_id</u>)
```PgSQL
CREATE TABLE note_video (
    note_id INTEGER NOT NULL,
    video_id INTEGER NOT NULL,
    FOREIGN KEY (note_id) REFERENCES note(note_id)
    ON DELETE CASCADE,
    FOREIGN KEY (video_id) REFERENCES video(video_id)
    ON DELETE CASCADE,
    PRIMARY KEY (note_id, video_id)
);
```

image(<u>image_id</u>, image_url)
```PgSQL
CREATE TABLE image (
    image_id SERIAL PRIMARY KEY,
    image_url VARCHAR(100) NOT NULL
);
```

problem_image(<u>problem_id</u>, <u>image_id</u>)
```PgSQL
CREATE TABLE problem_image (
    problem_id INTEGER NOT NULL,
    image_id INTEGER NOT NULL,
    FOREIGN KEY (problem_id) REFERENCES problem(problem_id)
    ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES image(image_id)
    ON DELETE CASCADE,
    PRIMARY KEY (problem_id, image_id)
);
```

note_image(<u>note_id</u>, <u>image_id</u>)
```PgSQL
CREATE TABLE note_image (
    note_id INTEGER NOT NULL,
    image_id INTEGER NOT NULL,
    FOREIGN KEY (note_id) REFERENCES note(note_id)
    ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES image(image_id)
    ON DELETE CASCADE,
    PRIMARY KEY (note_id, image_id)
);
```