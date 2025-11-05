package org.nbd.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee extends User { }