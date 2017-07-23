package core.aem.components;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;
import core.aem.MultifieldDetailsBean;
import core.constants.AppConstant;

/**
 * This is the class to retrieve details to be rendered in multifield component.
 * 
 * 
 */
public class DataIteratorComponent extends WCMUsePojo {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataIteratorComponent.class);

    private final List<MultifieldDetailsBean> dataIteratorList = new ArrayList<MultifieldDetailsBean>();

    /**
     * This is the activate method of the DataIteratorComponent class.
     * 
     */
    @Override
    public void activate() throws Exception {
        LOGGER.debug("DataIteratorComponent.activate Method started");

        try {

            if ((getResource().adaptTo(Node.class))
                    .hasNode(AppConstant.ITEMS_NODE)) {
                Node currentNode = (getResource().adaptTo(Node.class))
                        .getNode(AppConstant.ITEMS_NODE);
                if (currentNode != null) {

                    NodeIterator nodeIterator = currentNode.getNodes();

                    while (nodeIterator.hasNext()) {

                        Node detailsNode = nodeIterator.nextNode();
                        MultifieldDetailsBean multifieldDetailsBean = new MultifieldDetailsBean();
                        multifieldDetailsBean.setText(detailsNode.getProperty(
                                AppConstant.TEXT).getString());
                        multifieldDetailsBean
                                .setTextDescription(detailsNode.getProperty(
                                        AppConstant.TEXT_DESC).getString());
                        multifieldDetailsBean
                                .setSupportingImage(detailsNode.getProperty(
                                        AppConstant.IMAGE_REF).getString());
                        dataIteratorList.add(multifieldDetailsBean);

                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error in Multifield component", e.getMessage());
        }

    }

    /**
     * Get list of multifield component fields.
     * 
     * @return List of multifield component fields.
     */

    public List<MultifieldDetailsBean> getDataIteratorList() {
        return dataIteratorList;
    }

}
