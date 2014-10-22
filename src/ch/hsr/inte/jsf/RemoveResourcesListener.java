package ch.hsr.inte.jsf;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class RemoveResourcesListener 
  implements SystemEventListener {

  private static final String HEAD = "head";

  @Override
  public void processEvent(SystemEvent event) 
    throws AbortProcessingException {

    FacesContext context = FacesContext.getCurrentInstance();

    // Fetch included resources list size
    int i = context.getViewRoot().getComponentResources(context, HEAD)
          .size() - 1;

    while (i >= 0) {

      // Fetch current resource from included resources list
      UIComponent resource = context.getViewRoot()
            .getComponentResources(context, HEAD).get(i);

      // Fetch resource library and resource name
      String resourceLibrary = (String) resource.getAttributes().get(
          "library");
      String resourceName = (String) resource.getAttributes().get("name");

      // Check if we should remove the current resource.
      // Here we may check for any library and name combination.
      // (JSF, Primefaces, Richfaces, etc)
      if ("primefaces".equals(resourceLibrary) && resourceName.contains(".css")) {

        // Remove resource from view
        context.getViewRoot().removeComponentResource(context,
            resource, HEAD);
      }

      i--;

    }

  }
  @Override
  public boolean isListenerForSource(Object source) {
    return (source instanceof UIViewRoot);
  }

}