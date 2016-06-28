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

import com.ltbrew.brewbeer.persistence.greendao.DBBrewHistory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBBREW_HISTORY".
*/
public class DBBrewHistoryDao extends AbstractDao<DBBrewHistory, Long> {

    public static final String TABLENAME = "DBBREW_HISTORY";

    /**
     * Properties of entity DBBrewHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Formula_id = new Property(1, long.class, "formula_id", false, "FORMULA_ID");
        public final static Property Package_id = new Property(2, long.class, "package_id", false, "PACKAGE_ID");
        public final static Property Begin_time = new Property(3, String.class, "begin_time", false, "BEGIN_TIME");
        public final static Property End_time = new Property(4, String.class, "end_time", false, "END_TIME");
        public final static Property Pid = new Property(5, Integer.class, "pid", false, "PID");
        public final static Property State = new Property(6, Integer.class, "state", false, "STATE");
        public final static Property Ratio = new Property(7, Integer.class, "ratio", false, "RATIO");
        public final static Property Si = new Property(8, Integer.class, "si", false, "SI");
        public final static Property BrewingState = new Property(9, String.class, "brewingState", false, "BREWING_STATE");
        public final static Property St = new Property(10, String.class, "st", false, "ST");
        public final static Property Ms = new Property(11, Integer.class, "ms", false, "MS");
        public final static Property BrewingCmnMsg = new Property(12, String.class, "brewingCmnMsg", false, "BREWING_CMN_MSG");
        public final static Property ShowStepInfo = new Property(13, Boolean.class, "showStepInfo", false, "SHOW_STEP_INFO");
        public final static Property BrewingStageInfo = new Property(14, String.class, "brewingStageInfo", false, "BREWING_STAGE_INFO");
        public final static Property RecipeId = new Property(15, long.class, "recipeId", false, "RECIPE_ID");
    };

    private DaoSession daoSession;


    public DBBrewHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public DBBrewHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBBREW_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FORMULA_ID\" INTEGER NOT NULL UNIQUE ," + // 1: formula_id
                "\"PACKAGE_ID\" INTEGER NOT NULL UNIQUE ," + // 2: package_id
                "\"BEGIN_TIME\" TEXT," + // 3: begin_time
                "\"END_TIME\" TEXT," + // 4: end_time
                "\"PID\" INTEGER," + // 5: pid
                "\"STATE\" INTEGER," + // 6: state
                "\"RATIO\" INTEGER," + // 7: ratio
                "\"SI\" INTEGER," + // 8: si
                "\"BREWING_STATE\" TEXT," + // 9: brewingState
                "\"ST\" TEXT," + // 10: st
                "\"MS\" INTEGER," + // 11: ms
                "\"BREWING_CMN_MSG\" TEXT," + // 12: brewingCmnMsg
                "\"SHOW_STEP_INFO\" INTEGER," + // 13: showStepInfo
                "\"BREWING_STAGE_INFO\" TEXT," + // 14: brewingStageInfo
                "\"RECIPE_ID\" INTEGER NOT NULL );"); // 15: recipeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBBREW_HISTORY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBBrewHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getFormula_id());
        stmt.bindLong(3, entity.getPackage_id());
 
        String begin_time = entity.getBegin_time();
        if (begin_time != null) {
            stmt.bindString(4, begin_time);
        }
 
        String end_time = entity.getEnd_time();
        if (end_time != null) {
            stmt.bindString(5, end_time);
        }
 
        Integer pid = entity.getPid();
        if (pid != null) {
            stmt.bindLong(6, pid);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(7, state);
        }
 
        Integer ratio = entity.getRatio();
        if (ratio != null) {
            stmt.bindLong(8, ratio);
        }
 
        Integer si = entity.getSi();
        if (si != null) {
            stmt.bindLong(9, si);
        }
 
        String brewingState = entity.getBrewingState();
        if (brewingState != null) {
            stmt.bindString(10, brewingState);
        }
 
        String st = entity.getSt();
        if (st != null) {
            stmt.bindString(11, st);
        }
 
        Integer ms = entity.getMs();
        if (ms != null) {
            stmt.bindLong(12, ms);
        }
 
        String brewingCmnMsg = entity.getBrewingCmnMsg();
        if (brewingCmnMsg != null) {
            stmt.bindString(13, brewingCmnMsg);
        }
 
        Boolean showStepInfo = entity.getShowStepInfo();
        if (showStepInfo != null) {
            stmt.bindLong(14, showStepInfo ? 1L: 0L);
        }
 
        String brewingStageInfo = entity.getBrewingStageInfo();
        if (brewingStageInfo != null) {
            stmt.bindString(15, brewingStageInfo);
        }
        stmt.bindLong(16, entity.getRecipeId());
    }

    @Override
    protected void attachEntity(DBBrewHistory entity) {
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
    public DBBrewHistory readEntity(Cursor cursor, int offset) {
        DBBrewHistory entity = new DBBrewHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // formula_id
            cursor.getLong(offset + 2), // package_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // begin_time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // end_time
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // pid
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // state
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // ratio
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // si
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // brewingState
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // st
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // ms
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // brewingCmnMsg
            cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0, // showStepInfo
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // brewingStageInfo
            cursor.getLong(offset + 15) // recipeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBBrewHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFormula_id(cursor.getLong(offset + 1));
        entity.setPackage_id(cursor.getLong(offset + 2));
        entity.setBegin_time(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEnd_time(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPid(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setState(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setRatio(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setSi(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setBrewingState(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setSt(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setMs(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setBrewingCmnMsg(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setShowStepInfo(cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0);
        entity.setBrewingStageInfo(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setRecipeId(cursor.getLong(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBBrewHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBBrewHistory entity) {
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
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDBRecipeDao().getAllColumns());
            builder.append(" FROM DBBREW_HISTORY T");
            builder.append(" LEFT JOIN DBRECIPE T0 ON T.\"RECIPE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DBBrewHistory loadCurrentDeep(Cursor cursor, boolean lock) {
        DBBrewHistory entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DBRecipe dBRecipe = loadCurrentOther(daoSession.getDBRecipeDao(), cursor, offset);
         if(dBRecipe != null) {
            entity.setDBRecipe(dBRecipe);
        }

        return entity;    
    }

    public DBBrewHistory loadDeep(Long key) {
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
    public List<DBBrewHistory> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DBBrewHistory> list = new ArrayList<DBBrewHistory>(count);
        
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
    
    protected List<DBBrewHistory> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DBBrewHistory> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
