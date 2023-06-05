use myfirstdatabase;

select * from Principals;
select * from Teachers;
select * from StudentsWithGrades;

select count(*) from Principals where name = 'Anat' and password = 'pass_Anat_1';
select isLoggedIn from Principals where name = 'Anat' and password = 'pass_Anat_1';
UPDATE Principals SET isLoggedIn = 0 where name = 'Anat' and password = 'pass_Anat_1';