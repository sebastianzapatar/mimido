
TRUNCATE TABLE dishes RESTART IDENTITY CASCADE;
TRUNCATE TABLE chefs RESTART IDENTITY CASCADE;

-- Chefs
INSERT INTO chefs (name, description,specialty) VALUES
                                        ('Juan Pérez', "Mandar",'Italiana'),
                                        ('María Gómez', "Tacos",'Mexicana'),
                                        ('Carlos Ruiz', "Picana",'Parrilla'),
                                        ('Ana Torres', "Crembrule",'Pastelería'),
                                        ('Luisa Fernández', "Otra cosa",'Vegetariana');