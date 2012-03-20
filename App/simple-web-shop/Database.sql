/*
 * Database - shopping_cart
 * Server version: 5.0.32-Debian_7etch12-log
 * MySQL client version: 5.0.32
 *********************************************************************
*/
create database if not exists e0700180_shopping_cart;

USE e0700180_shopping_cart;


/*Table structure for table adminuser */

DROP TABLE IF EXISTS adminuser;

CREATE TABLE adminuser (
  id int(11) NOT NULL auto_increment,
  userid varchar(40) NOT NULL default '',
  password varchar(40) NOT NULL default '',
  PRIMARY KEY  (id),
  UNIQUE KEY userid (userid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table adminuser */

insert  into adminuser(id,userid,password) values (1,'admin','password');

/* Table structure for table category */

DROP TABLE IF EXISTS category;

CREATE TABLE category (
  categoryid int(11) NOT NULL default '0',
  categoryname varchar(60) default NULL,
  parentid int(11) NOT NULL default '0',
  PRIMARY KEY  (categoryid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table category */

insert  into category(categoryid,categoryname,parentid) 
values 
(1,'Automobiles',0),
(2,'Cellular',0),
(3,'Sport',0),
(4,'AudioVideo',0),
(5,'Beverages',0),
(8,'Mercedes',1),
(9,'Toyota',1),
(12,'In-line kates',3),
(13,'Snowboards',3),
(14,'Samsung',2),
(15,'Ericsson',2),
(17,'Nokia',2),
(18,'Siemens',2),
(20,'DVD',4),
(21,'mp3-players',4),
(23,'Books',0),
(24,'Lexus',1),
(25,'Clothes',0),
(27,'Computers',0),
(38,'Winter',25),
(39,'Summer',25),
(40,'Home',25),
(46,'Alcohol',5),
(47,'Alcohol free',5),
(51,'Notebooks',27),
(57,'PDA',27),
(58,'Personal',27),
(63,'Lotus',1),
(64,'Lamborghini',1),
(71,'Harry Potter',23),
(72,'Design',23),
(73,'Programming',23),
(74,'Classic',25),
(75,'Multimedia',27),
(76,'Home theatres',4),
(77,'Bicycles',3),
(78,'new  category',0),
(79,'new subcategory',78);

/*Table structure for table 'config' */

DROP TABLE IF EXISTS config;

CREATE TABLE config (
  id int(11) NOT NULL default '0',
  storename varchar(50) NOT NULL default '',
  storeurl varchar(50) NOT NULL default '',
  ordernoticeemail varchar(50) NOT NULL default '',
  cursymbol varchar(6) NOT NULL default '',
  curcode varchar(6) NOT NULL default '',
  aboutustext text NOT NULL,
  shipdeltext text NOT NULL,
  PRIMARY KEY  (id)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table 'config' */

insert  into config (id, storename, storeurl, ordernoticeemail, cursymbol, curcode, aboutustext, shipdeltext) values (1,'My Online 

Shop','http://www.cc.puv.fi/~e0700180','e0700180@puv.fi','€','EUR','Online shop simple project','Here you can provide information about shipping and payment 

options.This HTML text can be edited in the administrative mode.');

/*Table structure for table 'orderedcarts' */

DROP TABLE IF EXISTS orderedcarts;

CREATE TABLE orderedcarts (
  orderid int(11) NOT NULL auto_increment,
  line_number int(11) not null default '1', 
  productid int(11) NOT NULL default '0',
  customerid int(11) NOT NULL,
  ordertime datetime default NULL,
  productname varchar(60) default NULL,
  price varchar(20)default NULL,
  quantity varchar(11) default NULL,
  PRIMARY KEY  (orderid,line_number )
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table 'orderedcarts' 

insert  into orderedcarts (orderid,line_number,productid,customerid, ordertime, productname, price, quantity) 
values 
(1,1,'Toyota Land Cruiser 100',70000,2); */

/* Table structure for table 'orders' */

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  orderid int(11) NOT NULL auto_increment,
  ordertime datetime default NULL,
  customer_id int(11),
  custfirstname varchar(30) default NULL,
  custlastname varchar(30) default NULL,
  custemail varchar(30) default NULL,
  custcountry varchar(30) default NULL,
  custzip varchar(30) default NULL,
  custstate varchar(30) default NULL,
  custcity varchar(30) default NULL,
  custaddress varchar(30) default NULL,
  custphone varchar(30) default NULL,
  custfax int(11) default NULL,
  creditCardType varchar(30) NOT NULL default '',
  cardHolderName varchar(30) NOT NULL default '',
  creditCardNumber varchar(20) NOT NULL default '',
  expirationDate date NOT NULL default '0000-00-00',
  customerNotes varchar(255) NOT NULL default '',
  cvvNumber int(11) NOT NULL default '0',
  PRIMARY KEY  (orderid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/* Data for the table 'orders' */

insert  into orders (orderid, ordertime, custfirstname, custlastname,custemail, custcountry, custzip,custstate, custcity, custaddress, custphone, custfax,
creditCardType, cardHolderName, creditCardNumber, expirationDate, customerNotes, cvvNumber) 
values 
(1,'2011-01-12 09:38:18','Michael','Aro','finmichael@yahoo.com','Finland','65200','Vaasa','Vaasa','Palosaarentie 60 B 
120','449426274',4536789,'','','','2013-09-24','',0),
(5,NULL,'1','1','1','1','1','1','1','1','1',1,'1','1','1','2010-06-01','1',1),
(6,'2011-01-12 00:00:00','1','1','1','1','1','1','1','1','1',1,'1','1','1','2012-06-01','1',1),
(7,'2011-01-12 00:00:00','Ben','Affleck','ben.affleck@yahoo.com','Finland','65100','Seinajoki','Seinajoki','Palosaarentie 60 A 
104','449426275',543673,'','Ben','456789987654','2012-06-01','regular customer',733),
(8,'2011-01-12 00:00:00','Lee','Roberts','lee.roberts@gmail.com','USA','77388','TX','Spring','1245 Ellenwood','7132456345',543673,'MASTERCARD 
GOLD','Lee','456789987609','2014-06-01','regular customer',925),
(9,'2006-12-12 00:00:00','David','Shaw','davidshaw@yahoo.com','Finland','65200','Vaasa','Vaasa','Wolffintie 
30','3245454',5454554,'VISA','David','54654654','2014-06-01','',534534),
(10,'2011-01-12 00:00:00','Kenny','Chestnut','kchestnut@yahoo.com','USA','77489','TX','Katy','12 
Baywood','546576887',87786478,'DINNERS','Kenny','4343344334344334','2015-06-01','regular customer',345),
(11,'2011-01-10 19:01:12','Lou','Doulorfsky','','lou.doulorfsky@stanford.edu','67890','New Zealand','Auckland','Palo Alto','44 
XYZ',24324324,'MASTERCARD','Lou','53535345434','2015-06-01','',546),
(12,'2011-01-11 19:04:46','Wendy','Mao','wendy_mao@gmail.com','China','345353','Beijing','Taichi','32 
Guanzhou','24324324',334567,'VISA','Wendy','53535345434','2014-06-01','',546),
(13,'2007-06-23 19:10:38','Steven','Gorelick','steve.gorelick@biggle.com','Germany','5353534','Frankfurt','Frankfurt','5 Chrome 
Dr','54353',34535,'VISA','Steven','5353453','2012-06-01','',545),
(19,'2011-01-13 14:30:54','Mark','Zoback','mark@puv.fi','USA','94305','California','San Jose','23 Palo Alto 
Dr','6507232300',0,'ABC','234','2424242424242444','2007-02-09','234',23);

/*Table structure for table 'product' */

DROP TABLE IF EXISTS product;

CREATE TABLE product (
  productid int(11) NOT NULL auto_increment,
  categoryid int(11) NOT NULL default '0',
  productname varchar(60) NOT NULL default '',
  productprice varchar(40) NOT NULL default '',
  quantity varchar(11) NOT NULL default '',
  PRIMARY KEY  (productid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/* Data for the table 'product' */

insert  into product (productid, categoryid, productname, productprice, quantity) 
values 
(1,9,'Toyota Land Cruiser 100','70000','2'),
(2,9,'Toyota_98','80000','4'),
(3,8,'Mercedes S600','85990','2'),
(4,24,'Lexus GS300','5100','2'),
(5,24,'Lexus RX300','52000','2'),
(6,63,'Lotus Esprit','72000','2'),
(7,14,'Samsung R200','135','5'),
(8,14,'Samsung N100','157','6'),
(9,14,'Samsung A100','187','15'),
(10,15,'Ericsson T39','266','5'),
(11,15,'Ericsson T60','345','20'),
(12,14,'Samsung R20','125','10'),
(13,14,'Samsung n30','200','12'),
(14,74,'Classic01','50','10'),
(15,20,'The Last Kiss','15.30','5'),
(16,20,'Step Up(Widescreen)','23.42','20'),
(17,20,'Woh Lamhe','22.99','20'),
(18,20,'2 Hot 2 Handle','12.99','20'),
(19,76,'JVC LT 37X987 LCD HDTV','5000','2'),
(20,20,'GURU','9.00','20'),
(21,17,'Nokia 6600','250','40'),
(22,17,'Nokia 3230','250','10'),
(23,17,'Nokia N90','300','5'),
(24,17,'Nokia 91','625','5'),
(25,40,'Sweeter','47','10'),
(26,39,'Pants','19','5'),
(27,38,'Pants Winter','21','10'),
(28,51,'Fujitsu Siemens LifeBook P 111','1503','5'),
(29,51,'IBM ThinkPad T30 2366 89G','3170','5'),
(30,57,'3Com Palm m130','250','5'),
(31,12,'Roces Majestic 12','149','10'),
(32,13,'BURTON Balance','640','10'),
(33,13,'BURTON Cruzer','345','5'),
(34,13,'BURTON Power','659','5'),
(35,12,'Roces Khuti','179','5'),
(36,46,'Rum Captain organ','14','20'),
(37,47,'Coca Cola','0.99','50'),
(38,46,'Budwieser','0.39','50');

/* Table structure for table 'user' */

DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id int(11) NOT NULL auto_increment,
  userid varchar(30) NOT NULL default '0',
  password varchar(30) NOT NULL default '0',
  firstname varchar(30) default NULL,
  lastname varchar(30) default NULL,
  email varchar(30) default NULL,
  PRIMARY KEY  (id),
  UNIQUE KEY userid (userid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/* Data for the table 'user' */

insert  into user (id, userid, password, firstname, lastname, email) 
values (1,'mikie','mikie','Michael','Aro','finmichael@yahoo.com'),
(2,'mikie12','mikie','Michael','Aro','finmichael@yahoo.com'),
(3,'mikie122','mikie','Michael','Aro','finmichael@yahoo.com'),
(4,'mikie1222','mikie','Michael','Aro','finmichael@yahoo.com'),
(5,'mikie11','mikie','Michael','Aro','finmichael@yahoo.com'),
(6,'mikko','mikko','Michael','Aro','finmichael@yahoo.com');
