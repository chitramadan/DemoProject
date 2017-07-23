package core.aem.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import core.constants.AppConstant;

/**
 * This is the class to retrieve the list of the child pages for a given path .
 * 
 * 
 */
public class ListChildrenComponent extends WCMUsePojo {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ListChildrenComponent.class);

    private final List<String> listChildren = new ArrayList<String>();

    private ResourceResolver resourceResolver;

    /**
     * This is the activate method of the ListChildrenComponent class.
     * 
     */
    @Override
    public void activate() throws Exception {
        LOGGER.debug("ListChildrenComponent.activate Method started");

        try {
            resourceResolver = getServiceResourceResolver(
                    getSlingScriptHelper().getService(
                            ResourceResolverFactory.class), null);
            ValueMap properties = getResource().adaptTo(ValueMap.class);
            String parentPath = properties.get(AppConstant.PARENT_PATH,
                    String.class);
            if (resourceResolver != null && parentPath != null) {

                PageManager pageManager = resourceResolver
                        .adaptTo(PageManager.class);
                Page rootPage = pageManager.getPage(parentPath);

                Iterator<Page> childIterator = rootPage.listChildren();
                while (childIterator.hasNext()) {
                    Page childPage = childIterator.next();
                    listChildren.add(childPage.getName());
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error in ListChildren component", e.getMessage());
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }

    }

    /**
     * Get list child pages.
     * 
     * @return List of child pages.
     */

    public List<String> getChildrenList() {
        return listChildren;
    }

    /**
     * This method returns ResourceResolver for a given userservice.
     * 
     * @param resourceResolverFactory
     * @param userService
     * @return
     * @throws LoginException
     */
    private static ResourceResolver getServiceResourceResolver(
            ResourceResolverFactory resourceResolverFactory, String userService)
            throws LoginException {
        Map<String, Object> param = new HashMap<String, Object>();
        String userServiceId;
        if (userService == null) {
            userServiceId = AppConstant.READ_SERVICE_USER;
        } else {
            userServiceId = userService;
        }
        param.put(ResourceResolverFactory.SUBSERVICE, userServiceId);
        ResourceResolver resourceResolver = resourceResolverFactory
                .getServiceResourceResolver(param);
        return resourceResolver;
    }

}