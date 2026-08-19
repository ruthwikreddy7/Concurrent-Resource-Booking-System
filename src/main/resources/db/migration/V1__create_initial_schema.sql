CREATE TABLE theatres (
  theatre_id varchar(36) NOT NULL,
  address varchar(255) NOT NULL,
  city varchar(255) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  name varchar(255) NOT NULL,
  CONSTRAINT theatres_pkey PRIMARY KEY (theatre_id)
);

CREATE TABLE screens (
  screen_id varchar(36) NOT NULL,
  capacity integer NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  name varchar(255) NOT NULL,
  seats_per_row integer DEFAULT 10 NOT NULL,
  theatre_id varchar(36) NOT NULL,
  CONSTRAINT screens_pkey PRIMARY KEY (screen_id),
  CONSTRAINT fk_screens_theatre FOREIGN KEY (theatre_id) REFERENCES theatres(theatre_id)
);

CREATE TABLE seats (
  seat_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  row_label varchar(255) NOT NULL,
  seat_number integer NOT NULL,
  screen_id varchar(36) NOT NULL,
  CONSTRAINT seats_pkey PRIMARY KEY (seat_id),
  CONSTRAINT fk_seats_screen FOREIGN KEY (screen_id) REFERENCES screens(screen_id),
  CONSTRAINT uk_seat_screen_row_number UNIQUE (screen_id, row_label, seat_number)
);

CREATE TABLE movies (
  movie_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  duration_minutes integer NOT NULL,
  language varchar(255) NOT NULL,
  title varchar(255) NOT NULL,
  CONSTRAINT movies_pkey PRIMARY KEY (movie_id)
);

CREATE TABLE shows (
  show_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  end_time timestamp(6) with time zone NOT NULL,
  start_time timestamp(6) with time zone NOT NULL,
  movie_id varchar(36) NOT NULL,
  screen_id varchar(36) NOT NULL,
  CONSTRAINT shows_pkey PRIMARY KEY (show_id),
  CONSTRAINT fk_shows_movie FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
  CONSTRAINT fk_shows_screen FOREIGN KEY (screen_id) REFERENCES screens(screen_id)
);

CREATE TABLE show_seats (
  show_seat_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  status varchar(255) NOT NULL,
  updated_at timestamp(6) with time zone NOT NULL,
  version bigint NOT NULL,
  seat_id varchar(36) NOT NULL,
  show_id varchar(36) NOT NULL,
  CONSTRAINT show_seats_pkey PRIMARY KEY (show_seat_id),
  CONSTRAINT show_seats_status_check CHECK (status IN ('AVAILABLE', 'HELD', 'BOOKED')),
  CONSTRAINT fk_show_seats_seat FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
  CONSTRAINT fk_show_seats_show FOREIGN KEY (show_id) REFERENCES shows(show_id),
  CONSTRAINT uk_show_seat_show_physical_seat UNIQUE (show_id, seat_id)
);

CREATE TABLE booking_users (
  user_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  email varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  CONSTRAINT booking_users_pkey PRIMARY KEY (user_id),
  CONSTRAINT uk_booking_users_email UNIQUE (email)
);

CREATE TABLE bookings (
  booking_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  expires_at timestamp(6) with time zone NOT NULL,
  idempotency_key varchar(255),
  status varchar(255) NOT NULL,
  updated_at timestamp(6) with time zone NOT NULL,
  show_id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  CONSTRAINT bookings_pkey PRIMARY KEY (booking_id),
  CONSTRAINT bookings_status_check CHECK (status IN ('HELD', 'CONFIRMED', 'EXPIRED', 'CANCELLED')),
  CONSTRAINT fk_bookings_show FOREIGN KEY (show_id) REFERENCES shows(show_id),
  CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES booking_users(user_id)
);

CREATE TABLE booking_seats (
  booking_seat_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  booking_id varchar(36) NOT NULL,
  show_seat_id varchar(36) NOT NULL,
  CONSTRAINT booking_seats_pkey PRIMARY KEY (booking_seat_id),
  CONSTRAINT fk_booking_seats_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
  CONSTRAINT fk_booking_seats_show_seat FOREIGN KEY (show_seat_id) REFERENCES show_seats(show_seat_id)
);

CREATE TABLE booking_idempotency_records (
  idempotency_record_id varchar(36) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  idempotency_key varchar(128) NOT NULL,
  request_hash varchar(64) NOT NULL,
  response_body text NOT NULL,
  response_status integer NOT NULL,
  CONSTRAINT booking_idempotency_records_pkey PRIMARY KEY (idempotency_record_id),
  CONSTRAINT uk_booking_idempotency_key UNIQUE (idempotency_key)
);
