# code-to-scree

## frontend

### Vite installation

In the root directory:

```shell
npm create vite@latest
```

and the project is named `frontend`.

```shell
npm install
npm run dev
```

and the project is served at [http://localhost:5173/](http://localhost:5173/)

### Added Scala.js

* Added `sbt` configuration.

* Added `Main.scala` for code in scala.js

* Run `~fastLinkJS` in `sbt` and `npm run dev` on another terminal

### Added laminar dependency

* Added `laminar` as a dependency

* Changed `Main.scala` to use laminar

  * It seems that _something_ needs a `@main` entrypoint because leaving the code at top-level does not compile.
