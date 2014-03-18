IDEA:
=====
## top level navigation structure ##
koeb.me/about
koeb.me/contact
koeb.me/blog
koeb.me/intern
  ++ blocked for robots through robots.txt, http base auth, login required
  * manage website (and those links as well)
  * manage blog posts
  * additional features:
  * * koeb.me/intern/notes (manage notes)
  * * koeb.me/intern/projects (manage projects: clients, time, invoices)
  ** calendar and addressbook ... ??

## additional requirements ##
* basic CMS system
** HTML snippets as elements in basic CMS
** tags to include those snippets in an html page
** include via ajax or statically?
* plugin system for notes, blog, and my project management stuff

TASKS:
======
## basic cms ##
* install play, new app koeb.me
* CRUD for dynamic routes
* CRUD for dynamic pages mapped to routes
* CRUD for static unparsed resources
* add cache for parsed html pages and resources.
* controler to deliver page from cache or db or static, depending on dynamic route and page entry.
* plugin system

## notes as plugin ##
* CRUD for notes
* tag to show notes

##  blog as plugin ##
* change notes so they can be published.

## timetracking as plugin ##
* later

FEATURES:
=========
* fast
* dynamic linking between pages
* no static files in filesystem
* horizontally scalable
* everything except code is data driven
* hooks for metrics
* task queue for decoupling
* collect Links (like synchronized bookmarks)



