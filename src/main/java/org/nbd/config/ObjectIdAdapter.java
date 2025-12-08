package org.nbd.config;

import jakarta.json.bind.adapter.JsonbAdapter;
import org.bson.types.ObjectId;

public class ObjectIdAdapter implements JsonbAdapter<ObjectId, String> {

    @Override
    public  String adaptToJson(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    @Override
    public ObjectId adaptFromJson(String hexString) {
        return hexString != null && !hexString.isBlank() ? new ObjectId(hexString) : null;
    }
}
