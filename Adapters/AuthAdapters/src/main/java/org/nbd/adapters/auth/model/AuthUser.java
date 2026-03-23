package org.nbd.adapters.auth.model;

import lombok.Builder;
import org.bson.types.ObjectId;


@Builder
public record AuthUser (ObjectId id,String login,String role)
{

}
