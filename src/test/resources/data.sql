-- Inserting users
INSERT INTO users (name, email, password) VALUES
('Jonas Smith', 'jonas.smith@example.com', 'password123'),
('Mary Johnson', 'mary.johnson@example.com', 'password456'),
('Peter Brown', 'peter.brown@example.com', 'password789');

-- Inserting todo statuses
INSERT INTO todo_status (name) VALUES
('Pending'),
('In Progress'),
('Completed');

-- Inserting todos
INSERT INTO todos (title, description, user_id, status_id) VALUES
('Study SQL', 'Review joins, subqueries, and indexes', 1, 1),
('Finish report', 'Send weekly report to the manager', 2, 2),
('Buy supplies', 'Buy pens, notebooks, and folders', 1, 3),
('Practice programming', 'Solve algorithm exercises', 3, 1),
('Team meeting', 'Plan next steps of the project', 2, 2),
('Send email to client', 'Reply to product inquiries', 3, 3);