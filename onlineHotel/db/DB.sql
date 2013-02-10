use hoteldb;

CREATE TABLE `booking_details` (
  `BOOKING_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ROOM_NUMBER` varchar(20) NOT NULL,
  `CHECK_IN_DATE` date NOT NULL,
  `CHECK_OUT_DATE` date NOT NULL,
  `CUSTOMER_ID` int(11) NOT NULL,
  `PRICE_ON_BOOKING` double NOT NULL,
  `DISCOUNT_ON_BOOKING` double DEFAULT NULL,
  PRIMARY KEY (`BOOKING_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `charge` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DESCR` varchar(200) NOT NULL,
  `PRICE` double NOT NULL,
  `CHECK_IN_DETAILS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `check_in_details` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ROOM_NUMBER` varchar(20) NOT NULL,
  `CHECK_IN_TIME` date NOT NULL,
  `CHECK_OUT_TIME` date NOT NULL,
  `BOOKING_DETAILS_ID` int(11) NOT NULL,
  `ACTIVE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `contact_details` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PHONE_NUMBER` varchar(20) NOT NULL,
  `CELL_PHONE` varchar(20) DEFAULT NULL,
  `ADDRESS` varchar(200) NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `room` (
  `ROOM_NUMBER` varchar(20) NOT NULL,
  `NUM_OF_BEDS` int(11) NOT NULL,
  `PRICE` double NOT NULL,
  `DISCOUNT` double DEFAULT NULL,
  `DESCR` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ROOM_NUMBER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer` (
   ID INT UNSIGNED AUTO_INCREMENT NOT NULL,
   FIRST_NAME VARCHAR(20) NOT NULL,
   LAST_NAME VARCHAR(20) NOT NULL,
   CONTACT_DETAILS_ID INT NOT NULL,
   ID_CARD_NUMBER VARCHAR(20) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('201',2,50,30,'Simple 2 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('202',2,60,null,'Mountain view 2 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('203',2,70,50,'Sea view 2 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('301',3,55,null,'Simple 3 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('302',3,70,null,'Mountain view 3 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('303',3,85,null,'Sea view 3 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('401',4,75,null,'Simple 4 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('402',4,97,null,'Mountain view 4 bed room');
insert into `room`(`ROOM_NUMBER`,`NUM_OF_BEDS`,`PRICE`,`DISCOUNT`,`DESCR`) values ('403',4,110,null,'Sea view 4 bed room');
