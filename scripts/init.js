rs.status();
db.createUser({user: 'admin', pwd: 'admin', roles: [ { role: 'root', db: 'admin' } ]})

use admin;

db.createUser(
  {
    user: "cityBank",
    pwd: "K5q0Xezdqc0J",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
);
