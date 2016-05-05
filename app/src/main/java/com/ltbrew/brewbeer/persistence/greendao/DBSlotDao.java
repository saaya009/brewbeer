package com.ltbrew.brewbeer.persistence.greendao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.ltbrew.brewbeer.persistence.greendao.DBSlot;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBSLOT".
*/
public class DBSlotDao extends AbstractDao<DBSlot, Long> {

    public static final String TABLENAME = "DBSLOT";

    /**
     * Properties of entity DBSlot.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SlotStepId = new Property(1, String.class, "slotStepId", false, "SLOT_STEP_ID");
        public final static Property SlotId = new Property(2, String.class, "slotId", false, "SLOT_ID");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property RecipeId = new Property(4, long.class, "recipeId", false, "RECIPE_ID");
    };

    private DaoSession daoSession;

    private Query<DBSlot> dBRecipe_SlotsQuery;

    public DBSlotDao(DaoConfig config) {
        super(config);
    }
    
    public DBSlotDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBSLOT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SLOT_STEP_ID\" TEXT," + // 1: slotStepId
                "\"SLOT_ID\" TEXT," + // 2: slotId
                "\"NAME\" TEXT NOT NULL UNIQUE ," + // 3: name
                "\"RECIPE_ID\" INTEGER NOT NULL );"); // 4: recipeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBSLOT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBSlot entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String slotStepId = entity.getSlotStepId();
        if (slotStepId != null) {
            stmt.bindString(2, slotStepId);
        }
 
        String slotId = entity.getSlotId();
        if (slotId != null) {
            stmt.bindString(3, slotId);
        }
        stmt.bindString(4, entity.getName());
        stmt.bindLong(5, entity.getRecipeId());
    }

    @Override
    protected void attachEntity(DBSlot entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBSlot readEntity(Cursor cursor, int offset) {
        DBSlot entity = new DBSlot( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // slotStepId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // slotId
            cursor.getString(offset + 3), // name
            cursor.getLong(offset + 4) // recipeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBSlot entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSlotStepId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSlotId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.getString(offset + 3));
        entity.setRecipeId(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBSlot entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBSlot entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "slots" to-many relationship of DBRecipe. */
    public List<DBSlot> _queryDBRecipe_Slots(long recipeId) {
        synchronized (this) {
            if (dBRecipe_SlotsQuery == null) {
                QueryBuilder<DBSlot> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RecipeId.eq(null));
                dBRecipe_SlotsQuery = queryBuilder.build();
            }
        }
        Query<DBSlot> query = dBRecipe_SlotsQuery.forCurrentThread();
        query.setParameter(0, recipeId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDBRecipeDao().getAllColumns());
            builder.append(" FROM DBSLOT T");
            builder.append(" LEFT JOIN DBRECIPE T0 ON T.\"RECIPE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DBSlot loadCurrentDeep(Cursor cursor, boolean lock) {
        DBSlot entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DBRecipe dBRecipe = loadCurrentOther(daoSession.getDBRecipeDao(), cursor, offset);
         if(dBRecipe != null) {
            entity.setDBRecipe(dBRecipe);
        }

        return entity;    
    }

    public DBSlot loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<DBSlot> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DBSlot> list = new ArrayList<DBSlot>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<DBSlot> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DBSlot> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
