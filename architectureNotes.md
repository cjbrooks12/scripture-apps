# Clean Architecture

- Group be feature? Or group by component?
  - Neither! 
  - Apps are rarely so segmented that we can completely separate features. There will end up being some amount of 
    cross-domain integration necessary.
  - Grouping by component offers no structure or encapsulation between features.
  - What we need is a structure that does enforce separation between features, but with a way for features to access 
    functionality across application domain boundaries.
- Key point: define your application's models and features yourself 
  - APIs, DB ORM classes, etc. should never define or limit the functionality of your application code
  - Write and use the data classes you want for your app first. Then write "adapters" to convert the API responses to
    your hand-written data models.
- Proper layering and dependency flows
  - Start at the UI, not at the data source. Starting with the data source will cause your app to be reflective of (and
    limited by) the structure and capabilities of that data source, ultimately coupled to it. 
  - Write your screens using MVC, MVVM, MVI, etc. but stop at the boundary where anything "logical" happens. Don't
    make any DB queries, APi calls, etc. Instead, write a Use-Case which provides the UI with the data or actions it 
    needs. UI classes should not depend on one another, and should be completely isolated from each other. On Mobile
    form factors, isolate the UI by Screens. On Desktop form factors, depending on the complexity of the UI, it could be
    isolated by Page (simple pages) or Component (complex pages, especially for things like dashboards) or both (a 
    hierarchy of isolated components).
  - Mock the Use-Case out for now, but define the data models (inputs/outputs) as fully and correctly as you can, then
    build out the UI around that Use-Case.
  - Once the Use-Cases and data models have been defined, work can then be done in parallel on the other end of things.
    the UI team can build out the full UI and make it look nice, while another team can work on the data side of things.
    Build out the connections to APIs, DBs, etc. Create Repositories that encapsulate those data sources and map them 
    to the Data Models defined by the UI. Include _no_ logic within these repositories, it is a simple one-to-one 
    transformation from a data source to a data model.
  - Once the repository has been built out, we can then integrate the two. This is where we make the concrete Use-Case
    classes. To some extend, this can also be done in parallel to both the UI and the Data Sources. The Repositories 
    define an interface that produces or consumes the standard data models defined initially by the UI, and the 
    implementation of the repository can be done in parallel to the implementation of the use-case.
- build the whole thing with the idea of a homebuilder's blueprint. 
  - It's templting to think that defining all the classes for use-cases, repositories, etc. up front can sound very
    "waterfall-y", but it's not. All that we're doing is specifying the integration points
  - In the same way, building a home doesn't need to have all the details about the exact materials or processes needed
    to construct it. But they do need to know where the sewer line is in the slab. They need to know which rooms are
    bathrooms, bedrooms, etc. so they know where to run the plumbing and electricity. they need to know which walls are 
    structural and cannot be changed, and which ones are not and can be adjusted per the builder's preferences during
    construction.
