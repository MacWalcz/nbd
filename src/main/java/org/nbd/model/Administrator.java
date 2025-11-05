package org.nbd.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "administrators")
public class Administrator extends User { }
