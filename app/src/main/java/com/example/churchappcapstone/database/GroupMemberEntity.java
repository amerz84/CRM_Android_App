package com.example.churchappcapstone.database;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.example.churchappcapstone.utilities.Constants;

import java.util.Date;

@Entity(tableName = "group_members", primaryKeys = {"memberId", "groupId"},
        foreignKeys = { @ForeignKey(
                entity = MemberEntity.class,
                parentColumns = Constants.MEMBER_ID_COL,
                childColumns = Constants.GM_MEMBER_FK_COL,
                onDelete = ForeignKey.CASCADE),

                @ForeignKey( entity = GroupEntity.class,
                parentColumns = Constants.GROUP_ID_COL,
                childColumns = Constants.GM_GROUP_FK_COL,
                onDelete = ForeignKey.CASCADE)},
        indices = { @Index(value = Constants.GM_MEMBER_FK_COL), @Index(value = Constants.GM_GROUP_FK_COL)})
public class GroupMemberEntity {
    private int groupMemberId;
    private int groupId;
    private int memberId;
    private Date startDate;
    @Nullable
    private Date endDate;

    @Ignore
    public GroupMemberEntity() {}

    public GroupMemberEntity(int groupMemberId, int groupId, int memberId, Date startDate, Date endDate) {
        this.groupMemberId = groupMemberId;
        this.groupId = groupId;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(int groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
