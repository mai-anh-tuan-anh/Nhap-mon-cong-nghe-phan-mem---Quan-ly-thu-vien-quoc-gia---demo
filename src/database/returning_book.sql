-- =========================================================
-- NATIONAL LIBRARY MANAGEMENT SYSTEM
-- MODULE: RETURNING BOOKS
-- MySQL Script
-- =========================================================

DROP DATABASE IF EXISTS national_library_management_system;
CREATE DATABASE national_library_management_system;

USE national_library_management_system;

-- =========================================================
-- TABLE: tblUser
-- =========================================================

CREATE TABLE tblUser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullName VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- =========================================================
-- TABLE: tblReader
-- =========================================================

CREATE TABLE tblReader (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dateOfBirth DATE,
    address VARCHAR(255),
    phoneNumber VARCHAR(20),
    barcode VARCHAR(100) NOT NULL UNIQUE
);

-- =========================================================
-- TABLE: tblBook
-- =========================================================

CREATE TABLE tblBook (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    publicationYear YEAR,
    description TEXT,
    barcode VARCHAR(100) NOT NULL UNIQUE,
    price DECIMAL(12,2) NOT NULL
);

-- =========================================================
-- TABLE: tblBorrowingReceipt
-- =========================================================

CREATE TABLE tblBorrowingReceipt (
    id INT AUTO_INCREMENT PRIMARY KEY,
    barcode VARCHAR(100) NOT NULL UNIQUE,
    note TEXT,
    createdDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    depositAmount DECIMAL(12,2) DEFAULT 0,

    tblReaderId INT NOT NULL,
    tblUserId INT NOT NULL,

    CONSTRAINT fk_borrowingreceipt_reader
        FOREIGN KEY (tblReaderId)
        REFERENCES tblReader(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_borrowingreceipt_user
        FOREIGN KEY (tblUserId)
        REFERENCES tblUser(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================================================
-- TABLE: tblBorrowedBook
-- =========================================================

CREATE TABLE tblBorrowedBook (
    id INT AUTO_INCREMENT PRIMARY KEY,

    borrowDate DATE NOT NULL,
    dueDate DATE NOT NULL,
    price DECIMAL(12,2) NOT NULL,

    tblBookId INT NOT NULL,
    tblBorrowingReceiptId INT NOT NULL,

    CONSTRAINT fk_borrowedbook_book
        FOREIGN KEY (tblBookId)
        REFERENCES tblBook(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_borrowedbook_borrowingreceipt
        FOREIGN KEY (tblBorrowingReceiptId)
        REFERENCES tblBorrowingReceipt(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================================================
-- TABLE: tblReturningReceipt
-- =========================================================

CREATE TABLE tblReturningReceipt (
    id INT AUTO_INCREMENT PRIMARY KEY,

    barcode VARCHAR(100) NOT NULL UNIQUE,
    note TEXT,
    createdDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    tblReaderId INT NOT NULL,
    tblUserId INT NOT NULL,

    CONSTRAINT fk_returningreceipt_reader
        FOREIGN KEY (tblReaderId)
        REFERENCES tblReader(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_returningreceipt_user
        FOREIGN KEY (tblUserId)
        REFERENCES tblUser(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================================================
-- TABLE: tblReturnedBook
-- =========================================================

CREATE TABLE tblReturnedBook (
    id INT AUTO_INCREMENT PRIMARY KEY,

    returnDate DATE NOT NULL,

    tblReturningReceiptId INT NOT NULL,
    tblBorrowedBookId INT NOT NULL UNIQUE,

    CONSTRAINT fk_returnedbook_returningreceipt
        FOREIGN KEY (tblReturningReceiptId)
        REFERENCES tblReturningReceipt(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_returnedbook_borrowedbook
        FOREIGN KEY (tblBorrowedBookId)
        REFERENCES tblBorrowedBook(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================================================
-- TABLE: tblDamage
-- =========================================================

CREATE TABLE tblDamage (
    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL UNIQUE,
    fineRate DECIMAL(5,2) NOT NULL
);

-- =========================================================
-- TABLE: tblBookDamage
-- =========================================================

CREATE TABLE tblBookDamage (
    id INT AUTO_INCREMENT PRIMARY KEY,

    note TEXT,
    detectedDate DATE NOT NULL,
    fineAmount DECIMAL(12,2) NOT NULL,

    tblDamageId INT NOT NULL,
    tblReturnedBookId INT NOT NULL,

    CONSTRAINT fk_bookdamage_damage
        FOREIGN KEY (tblDamageId)
        REFERENCES tblDamage(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_bookdamage_returnedbook
        FOREIGN KEY (tblReturnedBookId)
        REFERENCES tblReturnedBook(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_reader_name
ON tblReader(name);

CREATE INDEX idx_book_name
ON tblBook(name);

CREATE INDEX idx_book_author
ON tblBook(author);

CREATE INDEX idx_borrowedbook_duedate
ON tblBorrowedBook(dueDate);

CREATE INDEX idx_returnedbook_returndate
ON tblReturnedBook(returnDate);

-- =========================================================
-- SAMPLE DATA FOR BLACKBOX TEST
-- =========================================================

-- =========================================================
-- tblUser
-- =========================================================

INSERT INTO tblUser(id, username, password, fullName, role)
VALUES
(1, 'a', 'a@123', 'Nguyen Van A', 'Librarian');

-- =========================================================
-- tblReader
-- =========================================================

INSERT INTO tblReader(id, name, dateOfBirth, address, phoneNumber, barcode)
VALUES
(1, 'B', '1997-01-01', 'Hà Nội', '123456789', '1111111111'),
(2, 'Bình', '2005-02-28', 'Hà Nội', '123123123', '1111111112'),
(3, 'xxx', '2011-11-11', 'Hà Nội', '123123456', '1111111113');

-- =========================================================
-- tblBook
-- =========================================================

INSERT INTO tblBook(
    id,
    name,
    code,
    author,
    publicationYear,
    description,
    barcode,
    price
)
VALUES
(
    1,
    'Harry Potter',
    '00001',
    'J.K Rowling',
    2000,
    'Good',
    '12345',
    10000
),
(
    2,
    'Diary of a cricket',
    '00002',
    'Tô Hoài',
    2000,
    'Dramatic',
    '12346',
    20000
),
(
    3,
    'Tấm Cám',
    '00003',
    'Unknown',
    2000,
    'Marvelous',
    '12347',
    30000
),
(
    4,
    'The lotus shoes',
    '00004',
    'Jane Yang',
    2000,
    'Incredible',
    '12348',
    40000
);

-- =========================================================
-- tblBorrowingReceipt
-- =========================================================

INSERT INTO tblBorrowingReceipt(
    id,
    barcode,
    note,
    createdDate,
    depositAmount,
    tblReaderId,
    tblUserId
)
VALUES
(
    1,
    '23456',
    'None',
    '2026-04-01 00:00:00',
    30000,
    1,
    1
),
(
    2,
    '23457',
    'None',
    '2026-04-15 00:00:00',
    70000,
    1,
    1
);

-- =========================================================
-- tblBorrowedBook
-- =========================================================

INSERT INTO tblBorrowedBook(
    id,
    borrowDate,
    dueDate,
    price,
    tblBookId,
    tblBorrowingReceiptId
)
VALUES
(
    1,
    '2026-04-01',
    '2026-05-01',
    10000,
    1,
    1
),
(
    2,
    '2026-04-01',
    '2026-05-01',
    20000,
    2,
    1
),
(
    3,
    '2026-04-15',
    '2026-05-15',
    30000,
    3,
    2
),
(
    4,
    '2026-04-15',
    '2026-05-15',
    40000,
    4,
    2
);

-- =========================================================
-- tblReturningReceipt
-- =========================================================

INSERT INTO tblReturningReceipt(
    id,
    barcode,
    note,
    createdDate,
    tblReaderId,
    tblUserId
)
VALUES
(
    1,
    '12345',
    'None',
    '2026-05-08 00:00:00',
    1,
    1
);

-- =========================================================
-- tblReturnedBook
-- =========================================================

INSERT INTO tblReturnedBook(
    id,
    returnDate,
    tblReturningReceiptId,
    tblBorrowedBookId
)
VALUES
(
    1,
    '2026-05-08',
    1,
    2
),
(
    2,
    '2026-05-08',
    1,
    3
);

-- =========================================================
-- tblDamage
-- =========================================================

INSERT INTO tblDamage(id, name, fineRate)
VALUES
(1, 'Torn', 90),
(2, 'Stain', 50),
(3, 'Graffiti', 50);

-- =========================================================
-- tblBookDamage
-- =========================================================

INSERT INTO tblBookDamage(
    id,
    note,
    detectedDate,
    fineAmount,
    tblDamageId,
    tblReturnedBookId
)
VALUES
(
    1,
    'Stain Damage',
    '2026-05-08',
    15000,
    2,
    2
),
(
    2,
    'Graffiti Damage',
    '2026-05-08',
    15000,
    3,
    2
);

-- =========================================================
-- RESET AUTO_INCREMENT
-- =========================================================

ALTER TABLE tblUser AUTO_INCREMENT = 2;
ALTER TABLE tblReader AUTO_INCREMENT = 4;
ALTER TABLE tblBook AUTO_INCREMENT = 5;
ALTER TABLE tblBorrowingReceipt AUTO_INCREMENT = 3;
ALTER TABLE tblBorrowedBook AUTO_INCREMENT = 5;
ALTER TABLE tblReturningReceipt AUTO_INCREMENT = 2;
ALTER TABLE tblReturnedBook AUTO_INCREMENT = 3;
ALTER TABLE tblDamage AUTO_INCREMENT = 4;
ALTER TABLE tblBookDamage AUTO_INCREMENT = 3;

-- =========================================================
-- DONE
-- =========================================================