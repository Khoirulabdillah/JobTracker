package com.example.jobtracker.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class JobDao_Impl implements JobDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<JobApplication> __insertionAdapterOfJobApplication;

  private final EntityDeletionOrUpdateAdapter<JobApplication> __deletionAdapterOfJobApplication;

  private final EntityDeletionOrUpdateAdapter<JobApplication> __updateAdapterOfJobApplication;

  public JobDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfJobApplication = new EntityInsertionAdapter<JobApplication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `applications` (`id`,`company`,`position`,`link`,`applyDate`,`status`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JobApplication entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCompany());
        statement.bindString(3, entity.getPosition());
        statement.bindString(4, entity.getLink());
        statement.bindLong(5, entity.getApplyDate());
        statement.bindString(6, __Status_enumToString(entity.getStatus()));
        statement.bindString(7, entity.getNotes());
      }
    };
    this.__deletionAdapterOfJobApplication = new EntityDeletionOrUpdateAdapter<JobApplication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `applications` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JobApplication entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfJobApplication = new EntityDeletionOrUpdateAdapter<JobApplication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `applications` SET `id` = ?,`company` = ?,`position` = ?,`link` = ?,`applyDate` = ?,`status` = ?,`notes` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JobApplication entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCompany());
        statement.bindString(3, entity.getPosition());
        statement.bindString(4, entity.getLink());
        statement.bindLong(5, entity.getApplyDate());
        statement.bindString(6, __Status_enumToString(entity.getStatus()));
        statement.bindString(7, entity.getNotes());
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final JobApplication job, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfJobApplication.insertAndReturnId(job);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final JobApplication job, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfJobApplication.handle(job);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final JobApplication job, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfJobApplication.handle(job);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<JobApplication>> observeAll() {
    final String _sql = "SELECT * FROM applications ORDER BY applyDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"applications"}, new Callable<List<JobApplication>>() {
      @Override
      @NonNull
      public List<JobApplication> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfLink = CursorUtil.getColumnIndexOrThrow(_cursor, "link");
          final int _cursorIndexOfApplyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "applyDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<JobApplication> _result = new ArrayList<JobApplication>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobApplication _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCompany;
            _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            final String _tmpPosition;
            _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            final String _tmpLink;
            _tmpLink = _cursor.getString(_cursorIndexOfLink);
            final long _tmpApplyDate;
            _tmpApplyDate = _cursor.getLong(_cursorIndexOfApplyDate);
            final Status _tmpStatus;
            _tmpStatus = __Status_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new JobApplication(_tmpId,_tmpCompany,_tmpPosition,_tmpLink,_tmpApplyDate,_tmpStatus,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<JobApplication> observeById(final long id) {
    final String _sql = "SELECT * FROM applications WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"applications"}, new Callable<JobApplication>() {
      @Override
      @Nullable
      public JobApplication call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfLink = CursorUtil.getColumnIndexOrThrow(_cursor, "link");
          final int _cursorIndexOfApplyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "applyDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final JobApplication _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCompany;
            _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            final String _tmpPosition;
            _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            final String _tmpLink;
            _tmpLink = _cursor.getString(_cursorIndexOfLink);
            final long _tmpApplyDate;
            _tmpApplyDate = _cursor.getLong(_cursorIndexOfApplyDate);
            final Status _tmpStatus;
            _tmpStatus = __Status_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _result = new JobApplication(_tmpId,_tmpCompany,_tmpPosition,_tmpLink,_tmpApplyDate,_tmpStatus,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final long id, final Continuation<? super JobApplication> $completion) {
    final String _sql = "SELECT * FROM applications WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<JobApplication>() {
      @Override
      @Nullable
      public JobApplication call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfLink = CursorUtil.getColumnIndexOrThrow(_cursor, "link");
          final int _cursorIndexOfApplyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "applyDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final JobApplication _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCompany;
            _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            final String _tmpPosition;
            _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            final String _tmpLink;
            _tmpLink = _cursor.getString(_cursorIndexOfLink);
            final long _tmpApplyDate;
            _tmpApplyDate = _cursor.getLong(_cursorIndexOfApplyDate);
            final Status _tmpStatus;
            _tmpStatus = __Status_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _result = new JobApplication(_tmpId,_tmpCompany,_tmpPosition,_tmpLink,_tmpApplyDate,_tmpStatus,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __Status_enumToString(@NonNull final Status _value) {
    switch (_value) {
      case DIKIRIM: return "DIKIRIM";
      case WAWANCARA: return "WAWANCARA";
      case PSIKOTES: return "PSIKOTES";
      case DITERIMA: return "DITERIMA";
      case DITOLAK: return "DITOLAK";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private Status __Status_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "DIKIRIM": return Status.DIKIRIM;
      case "WAWANCARA": return Status.WAWANCARA;
      case "PSIKOTES": return Status.PSIKOTES;
      case "DITERIMA": return Status.DITERIMA;
      case "DITOLAK": return Status.DITOLAK;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
