package com.aimprosoft.lfs.model.persist;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * The representation of mapping the {@link LookAndFeelBinding}
 * to current {@link User} and community
 *
 * @author AimProSoft
 */
@Entity
@Table(name = "ts_look_and_feel_binding")
public class LookAndFeelBinding extends PersistModel {
    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "look_and_feel_id")
    private LookAndFeel lookAndFeel;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "company_id")
    private Long companyId;

    @Transient
    private User user;
    @Transient
    private Group group;
    @Transient
    private String sessionId;



    public LookAndFeelBinding init() throws SystemException, PortalException {
        user = UserLocalServiceUtil.getUser(userId);
        group = GroupLocalServiceUtil.fetchGroup(groupId);
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }

    public void setLookAndFeel(LookAndFeel lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @JsonIgnore
    public boolean getBind() {
        return this.id != null;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"LookAndFeelBinding\", " +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"lookAndFeel\":" + (lookAndFeel == null ? "null" : "\"" + lookAndFeel + "\"") + ", " +
                "\"userId\":" + (userId == null ? "null" : "\"" + userId + "\"") + ", " +
                "\"groupId\":" + (groupId == null ? "null" : "\"" + groupId + "\"") + ", " +
                "\"companyId\":" + (companyId == null ? "null" : "\"" + companyId + "\"") + ", " +
                "\"sessionId\":" + (sessionId == null ? "null" : "\"" + sessionId + "\"") +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LookAndFeelBinding that = (LookAndFeelBinding) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public LookAndFeelBinding() {
    }

    public LookAndFeelBinding(ThemeDisplay themeDisplay) {
        userId = themeDisplay.getUserId();
        groupId = themeDisplay.getScopeGroupId();
        sessionId = themeDisplay.getSessionId();
        companyId = themeDisplay.getCompanyId();
    }

}