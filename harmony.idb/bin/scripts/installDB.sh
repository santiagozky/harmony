#!/bin/bash
#Script for uploading the db structure
# config #######################################################################
DUMPFILE=resources/database/DBdump.sql;
PROPERTIES=resources/properties/hibernate.properties;
LOGFILE=log/installDB.log
mkdir -p log

# Start ########################################################################

DEBUG="FALSE"

while [ 1 -le $# ]
do
    case "$1" in
        "--password")  
        		PASSWORD=$2 
        		TRUEFALSE="Y";;
        "--dbuser")
        		DATABASE_USER=$2 ;;
        "--dbuser_pwd")
        		DATABASE_USERPWD=$2 ;;
        "--database") 
        		DATABASE=$2 ;;
        "--host") 
        		DB_HOST=$2 ;;
        "--debug") 
        		DEBUG="TRUE" ;;
             *) ;;
	esac 
	shift
done


if [ "$DATABASE" == "" ]; then
  echo Usage: $0 --database NAME --dbuser DBUSER --dbuser_pwd DBUSER_PWD [--host HOST] [--password PWD] [--debug]
  exit;
fi

if [ "$DB_HOST" == "" ]; then
  DB_HOST="127.0.0.1";
fi

if [ "$PASSWORD" = "" ]; then
	if [ "$DATABASE_ROOTPWD" = "" ]; then
		echo  "You will need the root password for the mysql server";
		echo -n "Password: "
		stty -echo
		read PASSWORD
		stty echo
		echo ""  # force a carriage return to be output
	else
		PASSWORD="$DATABASE_ROOTPWD"
	fi
fi

if [ "$DATABASE_USER" == "" ]; then
  echo "You have to define a DATABASE_USER!";
  echo Usage: $0 --database NAME --dbuser DBUSER [--host HOST] [--password PWD] [--debug]
  exit;
fi

if [ "$DATABASE_USERPWD" == "" ]; then
  echo "You have to define a DATABASE_USERPWD!";
  echo Usage: $0 --database NAME --dbuser DBUSER --dbuser_pwd DBUSER_PWD [--host HOST] [--password PWD] [--debug]
  exit;
fi

echo "Connecting to '$DATABASE' on '$DB_HOST'...";

if echo "show databases;" | mysql --user=root --password=$PASSWORD --host=$DB_HOST >> $LOGFILE 2>> $LOGFILE ; then
        echo "Password Accepted";
        if [ "$TRUEFALSE" != "Y" ]; then
        	echo -n "Do you want to drop Database '$DATABASE' and user '$DATABASE_USER'? (Y/N): ";
        	read TRUEFALSE
		fi
        if [ "$TRUEFALSE" == "Y" ]; then
        		
                echo "Deleting Authorisation for database $DATABASE_NAME"
                echo "Deleting Authorisation for database $DATABASE_NAME" >> $LOGFILE;
                echo "delete from db where Db='$DATABASE_NAME';" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;

                echo "Deleting user $DATABASE_USER"
                echo "Deleting user $DATABASE_USER" >> $LOGFILE;
                echo "delete from user where user='$DATABASE_USER';" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;

                echo "Drop Database" $DATABASE;
                echo "Drop Database" $DATABASE >> $LOGFILE;
                echo "drop database " $DATABASE ";" | mysql --user=root --password=$PASSWORD --host=$DB_HOST 2>> $LOGFILE;

                echo  "Creating DB: " $DATABASE ;
                echo  "Creating DB: " $DATABASE >> $LOGFILE;
                echo "create database "$DATABASE";" | mysql --user=root --password=$PASSWORD --host=$DB_HOST 2>> $LOGFILE;

                echo  "Uploading Structure from file: " $DUMPFILE;
                echo  "Uploading Structure from file: " $DUMPFILE >> $LOGFILE;
                mysql --user=root --password=$PASSWORD --host=$DB_HOST $DATABASE < $DUMPFILE;

                echo "Adding user $DATABASE_USER to MySql table user";
                echo "Adding user $DATABASE_USER to MySql table user">> $LOGFILE;
                
                echo "insert into user (host, user, password) values('%','$DATABASE_USER',password('$DATABASE_USERPWD'));" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;
                echo "insert into user (host, user, password) values('localhost','$DATABASE_USER',password('$DATABASE_USERPWD'));" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;
                echo "insert into user (host, user, password) values('$HOSTNAME','$DATABASE_USER',password('$DATABASE_USERPWD'));" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;
                echo "insert into user (host, user, password) values('$DB_HOST','$DATABASE_USER',password('$DATABASE_USERPWD'));" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;
                
                echo "Setting privileges for user $DATABASE_USER";
                echo "Setting privileges for user $DATABASE_USER">> $LOGFILE;
                
                if [ "$DEBUG" == "TRUE" ]; then
                echo "insert into db (host,db,user,Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv) values ('%','$DATABASE','$DATABASE_USER','Y','Y','Y','Y','Y','Y');" 
				echo "insert into db (host,db,user,Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv) values ('localhost','$DATABASE','$DATABASE_USER','Y','Y','Y','Y','Y','Y');" 
				echo "insert into db (host,db,user,Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv) values ('$HOSTNAME','$DATABASE','$DATABASE_USER','Y','Y','Y','Y','Y','Y');" 
		fi
                
                echo "insert into db (host,db,user,Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv) values ('%','$DATABASE','$DATABASE_USER','Y','Y','Y','Y','Y','Y');" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;
				echo "insert into db (host,db,user,Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv) values ('localhost','$DATABASE','$DATABASE_USER','Y','Y','Y','Y','Y','Y');" | mysql --user=root --password=$PASSWORD --host=$DBHOST mysql 2>> $LOGFILE;
				echo "insert into db (host,db,user,Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv) values ('$HOSTNAME','$DATABASE','$DATABASE_USER','Y','Y','Y','Y','Y','Y');" | mysql --user=root --password=$PASSWORD --host=$DB_HOST mysql 2>> $LOGFILE;
				
                echo "Reloading privileges";
                echo "Reloading privileges" >> $LOGFILE;
                mysqladmin --user=root --password=$PASSWORD --host=$DB_HOST flush-privileges 2>> $LOGFILE;


                echo "Done!!";
                echo "Done!!" >> $LOGFILE;

        else
                echo "BYE"
        fi

else
        echo "Wrong password or no mysql client installed. Please make your checks! ";
        echo "Hint: look at $LOGFILE. Here is the last line:";
        tail -n1 $LOGFILE;
fi
