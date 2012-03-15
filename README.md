![/server logo](https://github.com/Footprint-Labs/slash-server/blob/master/doc/banner_logo.png?raw=true)
## What is /server
**/server** is an aggregation platform for building websites from distributed sources and programming languages, optimised for both desktop and mobile browsers. 

The idea was inspired by OpenSocial, specifically the gadget container where gadgets are aggregated and put together on a single page.  However unlike most OpenSocial containers, /server aggregates the content server side rather than in the browser.  We can do this because we make the assumption all the content providers come from a trusted source a luxury most OpenSocial containers can't make.

We've also decoupled the layout of a page and it's components from code allowing non-technical people to build and manage websites and freeing up the developers to focus on building dynamic components.  Thanks to the Zurb Foundation framework your sites will also look great on mobile devices.

### Lets start with how a /server page is put together

![The anatomy of a /server page](https://github.com/Footprint-Labs/slash-server/blob/master/doc/page_anatomy.png?raw=true)

### /server components
![/server components](https://github.com/Footprint-Labs/slash-server/blob/master/doc/server-components.png?raw=true)

* **AJAX Proxy** - As page snippets are served via /server, there would be a problem if client side javascript attempted to connect to services directly from the original server. To avoid hitting browser security restrictions we pipe ajax calls via /server. All pages include a jQuery plugin to transparently override the default ajax call, so if you are using jQuery you do not need to change anything!
* **Sitemaps** - Sitemaps are a list of pages for a given site, they are in a tree format and you can manage them directly via the RESTful API or build them in the admin console. The admin console shows a tree of pages for each site and you can drag and drop nodes in the tree to manage your sitemap hierarchy.  It's strongly advised to build any site navigation from the sitemap API so when pages are added, removed or moved changes are picked up automatically.
* **Page Rendering** - When a /server page is requested all the included snippets are fetched and aggregated into the final page. Any page parameters and also any snippet level app data is posted through to the snippet server for the request.
* **Snippet Data** - /server is all about dynamic, sophisticated content. So we provide a system to maintain the state of each of your snippets, so you dont need to. For example, you may have a page with several weather gadgets on it, each configurable so that the user can view the weather for a number of different regions. Our appdata service can save those settings and maintain them so the snippet server only needs to manage runtime parameters.
* **Other services** - All of the admin /server functions, for example the page designer and snippet admin tools, are built on restful services

## Building a page
### Page templates
Page templates contain the header and footer of the page.  They MUST contain a **&lt;slash-server/&gt;** tag at the point you want the snippets (including all the layout markup) injected into the page.  It's best to avoid putting too much markup in the page template as you'll miss out on the benefits of the Foundation layout handling. 

### Page layout manager
The page layout manager is where you design the layout of the page, select the snippets for the page and decide any special classes needed for the snippet.  You can also decide whether to hide individual snippets on mobile, tablets or desktop browsers.  It's built using the Zurb Foundation 12 column grid which helps ensure your pages look awesome from mobile and destop browsers.

### Snippets
Snippets are the bits of content that make up the page.  They are plain old html5 and can run on any domain with any technology.  You register snippets before you build a page that uses them. Anything in the **&lt;head&gt;** of your snippet will be included in the **&lt;head&gt;** of the rendered page. For security reasons we recommend only fetching snippets from domains you trust.

## Security with Users & Roles
/server is built using standard Java, Spring and hibernate.  We've implemented spring security and this can easily be extended to interface with any existing user management method eg LDAP, Database, Active directory etc.  Pages are assigned roles and the sitemap will only return pages for which the current user is permissioned to see.  There is a minimum of two roles admin and public.  If you are adding a new page in a production environment you can test with the admin role and once you'er ready to launch switch it to another role with the required audience.  

If your snippets are protected (eg using SSO) then you may need to protect your /server site with the same method.  When /server requests a snippet it will pass all the headers on to the snippet provider which effectively makes the request on the users behalf.  
