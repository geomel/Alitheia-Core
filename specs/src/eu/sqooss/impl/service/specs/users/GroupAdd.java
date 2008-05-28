package eu.sqooss.impl.service.specs.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import eu.sqooss.impl.service.SpecsActivator;
import eu.sqooss.service.db.DBService;
import eu.sqooss.service.db.Group;
import eu.sqooss.service.db.GroupPrivilege;
import eu.sqooss.service.db.User;
import eu.sqooss.service.security.GroupManager;

@RunWith(ConcordionRunner.class)
public class GroupAdd
{
    private DBService db = SpecsActivator.alitheiaCore.getDBService();

    public void addGroup(String groupName)
    {
        GroupManager gm = SpecsActivator.alitheiaCore.getSecurityManager().getGroupManager();
        db.startDBSession();
        gm.createGroup(groupName);
        db.commitDBSession();
    }
    
    public ArrayList<String> getGroups()
    {
        ArrayList<String> result = new ArrayList<String>();
        
        db.startDBSession();
        List<Group> groups = db.findObjectsByProperties(Group.class, new HashMap<String,Object>());
        
        for (Group group : groups)
        {
            result.add(group.getDescription());
        }
        db.commitDBSession();
        
        return result;
    }
    
    public boolean groupHasNoPrivilege(String groupName)
    {
        db.startDBSession();
        HashMap<String,Object> properties = new HashMap<String,Object>();
        properties.put("description", groupName);
        List<Group> groups = db.findObjectsByProperties(Group.class, properties);
        
        if (groups.size()!=1) throw new RuntimeException("We should have only one element in this list!");
        
        boolean hasPrivileges = !groups.get(0).getGroupPrivileges().isEmpty();
        
        if (hasPrivileges)
        {
            Group group = groups.get(0);
            for (Object obj : group.getGroupPrivileges())
            {
                GroupPrivilege priv = (GroupPrivilege)obj;

                String privValue = priv.getPv().getValue(); 
                if (priv.getPv().getPrivilege().getDescription().equals("user_id"))
                {
                    User user = db.findObjectById(User.class, Long.valueOf(privValue));
                    privValue = user.getName();
                }
                
                System.out.println(
                        group.getDescription()+", "+
                        priv.getUrl().getUrl()+", "+
                        priv.getPv().getPrivilege().getDescription()+", "+
                        privValue
                );
            }

        }
        
        db.commitDBSession();
        
        return !hasPrivileges;
    }
}
