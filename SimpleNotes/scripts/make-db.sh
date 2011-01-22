#!/bin/bash

if [ -z $1 ]; then
    DB_FILE=notes.db
else
    DB_FILE=$1
fi

if [ -e $DB_FILE ]; then
    echo "$DB_FILE already exists."
    exit 1
fi

sqlite3 $DB_FILE <<EOF
CREATE TABLE android_metadata (locale TEXT);
insert into android_metadata values('en_US');

CREATE TABLE note (_id integer primary key autoincrement,
                   key text,
                   content text not null,
                   modifydate text,
                   createdate text,
                   syncnum integer,
                   version integer,
                   minversion integer,
                   sharekey text,
                   publishkey text,
                   deleted integer not null default 0,
                   pinned integer not null default 0,
                   unread integer not null default 0);
CREATE TABLE tag (_id integer primary key autoincrement,
                  name text not null);
CREATE TABLE relation (_id integer primary key autoincrement,
                       noteid integer not null,
                       tagid integer not null,
                       idx integer not null);


insert into note (_id, content) values (0, 'Take me to your leader! [having quickly written a book to trap the Big Brain in] There, now he''s trapped in a book I wrote: a crummy world of plot holes and spelling errors! You know the worst thing about being a slave? They make you work, but they don''t pay you or let you go. Yeah, and if you were the pope they''d be all, "Straighten your pope hat." And "Put on your good vestments." That''s right, baby. I ain''t your loverboy Flexo, the guy you love so much. You even love anyone pretending to be him!');
insert into tag values (0, 'long');
insert into relation (noteid, tagid, idx) values (0, 0, 0);

insert into note (_id, content) values(1, 'This is a short note.');
insert into tag values(1, 'short');
insert into relation (noteid, tagid, idx) values (1, 1, 0);

pragma user_version = 1;
EOF
