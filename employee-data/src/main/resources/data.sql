INSERT INTO DEPARTMENT (ID, NAME, DESCRIPTION) VALUES
  (1, 'Bugs', 'Departamento de bugs'),
  (2, 'Inglês', 'Departamento de inglês'),
  (3, 'Anti-fraudes', 'Departamento anti-fraudes'),
  (4, 'Metalurgia', 'Departamento de metalurgia');

INSERT INTO EMPLOYEE (UUID, CPF, NAME, PHONE, EMAIL, AGE, DEPARTMENT_ID) VALUES
  ('6580fc2d-0c73-42ba-b301-471a90887970','72840375044', 'John Lennon', '+5511912345678', 'john.lennon@email.com', 40, 1),
  ('88edcde2-ddf6-4fa4-a7c8-0fa8abe8a69f','75944007044', 'Ringo Starr', '+5511912345679', 'ringo.starr@email.com', 79, 1),
  ('67855698-92bf-4842-966b-e33ea2d34133','02836977094', 'Paul McCartney', '+5511912345670', 'paul.mccartney@email.com', 77, 1),
  ('e4c0fa37-8d85-494c-9959-43279a402d2b','66872648007', 'George Harrison', '+5511912345671', 'george.harrison@email.com', 58, 1),
  ('9d75b88e-b1ac-4965-93bd-07514d9eba83','80171214072', 'Brian May', '+5512912345678', 'brian.may@email.com', 72, 2),
  ('876e9101-c3be-49d3-a2f9-121d244597a3','38675765053', 'Freddie Mercury', '+5512912345679', 'freddie.mercury@email.com', 45, 2),
  ('a9ce1cac-7090-41ce-aa70-d6cc0ebe5728','52359180029', 'John Deacon', '+5512912345670', 'john.deacon@email.com', 68, 2),
  ('c4d5fe11-ab02-4f12-b200-5cb74ebd69a3','99170096040', 'Roger Taylor', '+5512912345671', 'roger.taylor@email.com', 70, 2),
  ('dd79fe5e-cb5f-434e-bc3e-504fcbcaee58','62993721097', 'Josh Kiszka', '+5513912345678', 'josh.kiszka@email.com', 23, 3),
  ('2544abcf-13e1-4238-ac0a-5fcd5f9a4710','79163158078', 'Jake Kiszka', '+5513912345679', 'jake.kiszka@email.com', 20, 3),
  ('a23c4aa3-4d88-4f1c-9f7e-1aa65d136460','19410260019', 'Sam Kiszka', '+5513912345670', 'sam.kiszka@email.com', 23, 3),
  ('8e13e89d-7ab9-4945-9a15-89555412202a','00922837058', 'Daniel Wagner', '+5513912345671', 'daniel.wagner@email.com', 19, 3),
  ('d7f072e5-6c94-4380-8f0c-3e6db1da70a0','79901177030', 'Corey Taylor', '+5514912345678', 'corey.taylor@email.com', 45, 4),
  ('c83a2b36-115f-4d7b-af54-2afe5770d4a2','92581573031', 'Mick Thomson', '+5514912345679', 'mick.thomson@email.com', 46, 4),
  ('48e6f83f-dac6-443e-8e66-fc67708a8259','56722104060', 'Shawn Crahan', '+5514912345670', 'shawn.crahan@email.com', 50, 4),
  ('7b1a53fe-d2fe-43b1-a192-6a11a9881cc6','98754037093', 'Craig Jones', '+5514912345670', 'craig.jones@email.com', 47, 4),
  ('6e6c0d14-fc07-4447-b7f9-1234cbb2fe5d','10161493017', 'Sid Wilson', '+5514912345671', 'sid.wilson@email.com', 42, 4),
  ('39340eab-a581-4624-b90c-ef9e8080e6a5','62602893080', 'James Root', '+5514912345678', 'james.root@email.com', 48, 4),
  ('90526909-dcdb-4ca8-a1fa-ce37831ba46b','70644090030', 'Jay Weinberg', '+5514912345679', 'jay.weinberg@email.com', 29, 4),
  ('7793ff48-b7cd-4916-9854-8bf8423368e8','52432851021', 'Alessandro Venturella', '+5514912345670', 'alessandro.Venturella@email.com', 32, 4);