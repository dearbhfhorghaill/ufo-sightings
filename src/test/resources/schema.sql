create table if not exists sightings (
    id int not null,
    datetime datetime,
    city varchar(255),
    state varchar(255),
    country varchar(255),
    shape varchar(255),
    duration_seconds int,
    duration_hours_mins varchar(255),
    comments text,
    date_posted datetime,
    latitude float,
    longitude float,
    agent varchar(255),
    primary key (id)
);

INSERT INTO sightings (
    id,
    datetime,
    city,
    state,
    country,
    shape,
    duration_seconds,
    duration_hours_mins,
    comments,
    date_posted,
    latitude,
    longitude,
    agent
)
VALUES
(100, '2025-01-01 21:15:00', 'Los Angeles', 'CA', 'US', 'circle', 120, '2 minutes', 'Bright light hovering in the sky.', '2025-01-02 10:00:00', 34.0522, -118.2437, 'mulder'),
(101, '2025-01-05 23:30:00', 'Phoenix', 'AZ', 'US', 'triangle', 300, '5 minutes', 'Three lights forming a triangular shape.', '2025-01-06 08:45:00', 33.4484, -112.0740, 'mulder'),
(102, '2025-01-10 20:45:00', 'Seattle', 'WA', 'US', 'disk', 600, '10 minutes', 'Disk-shaped object moving silently across the sky.', '2025-01-11 12:30:00', 47.6062, -122.3321, 'mulder'),
(103, '2025-01-15 19:00:00', 'Miami', 'FL', 'US', 'fireball', 30, '30 seconds', 'Fireball streaking across the horizon.', '2025-01-16 09:15:00', 25.7617, -80.1918, 'scully'),
(104, '2025-01-20 22:00:00', 'Denver', 'CO', 'US', 'cigar', 180, '3 minutes', 'Cigar-shaped craft hovering above the mountains.', '2025-01-21 07:50:00', 39.7392, -104.9903, 'scully');
