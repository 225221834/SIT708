package com.example.istream.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<User> __insertAdapterOfUser;

  private final EntityInsertAdapter<PlaylistItem> __insertAdapterOfPlaylistItem;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUser = new EntityInsertAdapter<User>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`fullName`,`username`,`password`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFullName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getFullName());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getUsername());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getPassword());
        }
      }
    };
    this.__insertAdapterOfPlaylistItem = new EntityInsertAdapter<PlaylistItem>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `playlist` (`id`,`userId`,`youtubeUrl`,`title`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final PlaylistItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        if (entity.getYoutubeUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getYoutubeUrl());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getTitle());
        }
      }
    };
  }

  @Override
  public long insertUser(final User user) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfUser.insertAndReturnId(_connection, user);
    });
  }

  @Override
  public void insertPlaylistItem(final PlaylistItem item) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfPlaylistItem.insert(_connection, item);
      return null;
    });
  }

  @Override
  public User login(final String username, final String password) {
    final String _sql = "SELECT * FROM users WHERE username = ? AND password = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        _argIndex = 2;
        if (password == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, password);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFullName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fullName");
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final User _result;
        if (_stmt.step()) {
          _result = new User();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _result.setId(_tmpId);
          final String _tmpFullName;
          if (_stmt.isNull(_columnIndexOfFullName)) {
            _tmpFullName = null;
          } else {
            _tmpFullName = _stmt.getText(_columnIndexOfFullName);
          }
          _result.setFullName(_tmpFullName);
          final String _tmpUsername;
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _tmpUsername = null;
          } else {
            _tmpUsername = _stmt.getText(_columnIndexOfUsername);
          }
          _result.setUsername(_tmpUsername);
          final String _tmpPassword;
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null;
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword);
          }
          _result.setPassword(_tmpPassword);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public User checkUsernameExists(final String username) {
    final String _sql = "SELECT * FROM users WHERE username = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFullName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fullName");
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final User _result;
        if (_stmt.step()) {
          _result = new User();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _result.setId(_tmpId);
          final String _tmpFullName;
          if (_stmt.isNull(_columnIndexOfFullName)) {
            _tmpFullName = null;
          } else {
            _tmpFullName = _stmt.getText(_columnIndexOfFullName);
          }
          _result.setFullName(_tmpFullName);
          final String _tmpUsername;
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _tmpUsername = null;
          } else {
            _tmpUsername = _stmt.getText(_columnIndexOfUsername);
          }
          _result.setUsername(_tmpUsername);
          final String _tmpPassword;
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _tmpPassword = null;
          } else {
            _tmpPassword = _stmt.getText(_columnIndexOfPassword);
          }
          _result.setPassword(_tmpPassword);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<PlaylistItem>> getPlaylistForUser(final int userId) {
    final String _sql = "SELECT * FROM playlist WHERE userId = ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"playlist"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfYoutubeUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youtubeUrl");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final List<PlaylistItem> _result = new ArrayList<PlaylistItem>();
        while (_stmt.step()) {
          final PlaylistItem _item;
          _item = new PlaylistItem();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          final int _tmpUserId;
          _tmpUserId = (int) (_stmt.getLong(_columnIndexOfUserId));
          _item.setUserId(_tmpUserId);
          final String _tmpYoutubeUrl;
          if (_stmt.isNull(_columnIndexOfYoutubeUrl)) {
            _tmpYoutubeUrl = null;
          } else {
            _tmpYoutubeUrl = _stmt.getText(_columnIndexOfYoutubeUrl);
          }
          _item.setYoutubeUrl(_tmpYoutubeUrl);
          final String _tmpTitle;
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null;
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle);
          }
          _item.setTitle(_tmpTitle);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public PlaylistItem checkUrlExists(final int userId, final String url) {
    final String _sql = "SELECT * FROM playlist WHERE userId = ? AND youtubeUrl = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        _argIndex = 2;
        if (url == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, url);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfYoutubeUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youtubeUrl");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final PlaylistItem _result;
        if (_stmt.step()) {
          _result = new PlaylistItem();
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _result.setId(_tmpId);
          final int _tmpUserId;
          _tmpUserId = (int) (_stmt.getLong(_columnIndexOfUserId));
          _result.setUserId(_tmpUserId);
          final String _tmpYoutubeUrl;
          if (_stmt.isNull(_columnIndexOfYoutubeUrl)) {
            _tmpYoutubeUrl = null;
          } else {
            _tmpYoutubeUrl = _stmt.getText(_columnIndexOfYoutubeUrl);
          }
          _result.setYoutubeUrl(_tmpYoutubeUrl);
          final String _tmpTitle;
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null;
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle);
          }
          _result.setTitle(_tmpTitle);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deletePlaylistItem(final int itemId, final int userId) {
    final String _sql = "DELETE FROM playlist WHERE id = ? AND userId = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, itemId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, userId);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
