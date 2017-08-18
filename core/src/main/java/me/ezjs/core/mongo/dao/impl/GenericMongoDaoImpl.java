package me.ezjs.core.mongo.dao.impl;

import me.ezjs.core.mongo.dao.GenericMongoDao;
import me.ezjs.core.mongo.model.RootMongoObject;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by zero-mac on 17/7/19.
 */
public class GenericMongoDaoImpl implements GenericMongoDao {

    private static final Logger log = Logger.getLogger(GenericMongoDaoImpl.class);

    MongoTemplate mongoTemplate;

    private GenericMongoDaoImpl() {
    }

    public GenericMongoDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void create(RootMongoObject obj) {
        try {
            mongoTemplate.insert(obj);
        } catch (Exception e) {
            try {
                mongoTemplate.insert(obj);
            } catch (Exception e2) {
                log.error(e2);
            }
        }
    }
}
