USE fantasy;

CREATE TABLE league (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_id INT NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100),
    logo VARCHAR(500),
    type VARCHAR(30),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE team (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_id INT NOT NULL UNIQUE,
    league_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10),
    logo VARCHAR(500),
    country VARCHAR(100),
    founded INT,
    national BOOLEAN,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_team_league
        FOREIGN KEY (league_id)
        REFERENCES league(id)
);

CREATE TABLE player (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_id INT NOT NULL UNIQUE,
    team_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    photo VARCHAR(500),
    position VARCHAR(20),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_player_team
        FOREIGN KEY (team_id)
        REFERENCES team(id)
);

CREATE TABLE fixture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_id INT NOT NULL UNIQUE,
    league_id BIGINT NOT NULL,
    season INT NOT NULL,
    home_team_id BIGINT NOT NULL,
    away_team_id BIGINT NOT NULL,
    date DATETIME NOT NULL,
    status VARCHAR(20),
    home_goals INT,
    away_goals INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_fixture_league
        FOREIGN KEY (league_id)
        REFERENCES league(id),

    CONSTRAINT fk_fixture_home_team
        FOREIGN KEY (home_team_id)
        REFERENCES team(id),

    CONSTRAINT fk_fixture_away_team
        FOREIGN KEY (away_team_id)
        REFERENCES team(id)
);

CREATE TABLE player_fixture_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    player_id BIGINT NOT NULL,
    fixture_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,

    minutes INT,
    number INT,
    position VARCHAR(20),
    rating DECIMAL(3,1),
    captain BOOLEAN NOT NULL DEFAULT FALSE,
    substitute BOOLEAN NOT NULL DEFAULT FALSE,

    fantasy_points INT NOT NULL DEFAULT 0,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_stats_player
        FOREIGN KEY (player_id)
        REFERENCES player(id),

    CONSTRAINT fk_stats_fixture
        FOREIGN KEY (fixture_id)
        REFERENCES fixture(id),

    CONSTRAINT fk_stats_team
        FOREIGN KEY (team_id)
        REFERENCES team(id),

    CONSTRAINT uk_player_fixture
        UNIQUE (player_id, fixture_id)
);

CREATE TABLE player_fixture_attack (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    shots_total INT,
    shots_on INT,

    goals INT,
    assists INT,

    offsides INT,

    dribbles_attempts INT,
    dribbles_success INT,
    dribbles_past INT,

    CONSTRAINT fk_attack_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);

CREATE TABLE player_fixture_passing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    passes_total INT,
    passes_key INT,
    passes_accuracy INT,

    CONSTRAINT fk_passing_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);

CREATE TABLE player_fixture_defense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    tackles_total INT,
    tackles_blocks INT,
    tackles_interceptions INT,

    duels_total INT,
    duels_won INT,

    CONSTRAINT fk_defense_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);

CREATE TABLE player_fixture_fouls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    fouls_drawn INT,
    fouls_committed INT,

    CONSTRAINT fk_fouls_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);

CREATE TABLE player_fixture_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    yellow_cards INT,
    red_cards INT,

    CONSTRAINT fk_cards_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);

CREATE TABLE player_fixture_goalkeeping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    goals_conceded INT,
    goals_saves INT,

    CONSTRAINT fk_goalkeeping_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);

CREATE TABLE player_fixture_penalty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    stats_id BIGINT NOT NULL UNIQUE,

    penalty_won INT,
    penalty_committed INT,
    penalty_scored INT,
    penalty_missed INT,
    penalty_saved INT,

    CONSTRAINT fk_penalty_stats
        FOREIGN KEY (stats_id)
        REFERENCES player_fixture_stats(id)
        ON DELETE CASCADE
);
