package com.example.churchappcapstone.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.churchappcapstone.utilities.Constants;

@Entity(tableName = "groups",
        foreignKeys = { @ForeignKey(
                entity = MemberEntity.class,
                parentColumns = Constants.MEMBER_ID_COL,
                childColumns = Constants.GROUP_FK_COL,
                onDelete = ForeignKey.SET_DEFAULT)},
        indices = { @Index(value = Constants.GROUP_FK_COL)})
public class GroupEntity {
    @PrimaryKey(autoGenerate = true)
    private int groupId;
    private String groupName;
    @ColumnInfo(name = "groupChairpersonId", defaultValue = "deleted")
    private int groupChairpersonId;

    @Ignore
    public GroupEntity() {}

    public GroupEntity(String groupName, int groupChairpersonId) {
        this.groupName = groupName;
        this.groupChairpersonId = groupChairpersonId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupChairpersonId() {
        return groupChairpersonId;
    }

    public void setGroupChairpersonId(int groupChairpersonId) {
        this.groupChairpersonId = groupChairpersonId;
    }
}
