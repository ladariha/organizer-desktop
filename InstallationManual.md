# Installation Manual #

## Prerequisites ##
To launch Organizer Desktop you need to install:
  1. Java Runtime Environment (or Java Development Kit)
  1. MySQL Database

### Java Runtime Environment ###
You can download from http://java.com/en/download/manual.jsp. This site also contains instructions for installation.

### External libraries ###
Project requires some external libraries. They are all in main file OrganizerDesktop.zip.

Links to some used libraries:
  1. Image Scaling: http://code.google.com/p/java-image-scaling/
  1. Charset detector: http://jchardet.sourceforge.net/

Other libraries:
  1. Hibernate, Google API...

### MySQL Database ###
You can either download just database from http://dev.mysql.com/downloads/ or you can download same software, that often contains more services like web server Apache, FTP client FileZilla or other components. Personally, I'm using XAMPP (avaliable at http://www.apachefriends.org/en/xampp.html) and it works perfectly. XAMPP supports all platforms (Windows, Mac, Linux or Solaris).

Because this application uses Hibernate to work with database, by changing Hibernate configuration file you can use more databases (Oracle, PostreSQL ...). But currently it is not supported by default. Maybe later :)

In your database create database called organizer and user called ODBC with password odbc. If you want to use different settings, then modify hibernate config file (file in directory src/hibernate.cfg.xml).

Hibernate will automatically create all tables in this database.