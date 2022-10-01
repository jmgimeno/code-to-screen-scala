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

## backend

* Added configuration in `build.sbt`

* Minimal "test" on browser console (in many tabs) by:

```javascript
let ws = new WebSocket('ws://localhost:8080/subscribe')
ws.onmessage = function(e) { console.log(e) }
ws.send("potato")
```

## modified frontend to use websockets

* Added dependency on 'laminext'

* Added proxy in `vite.config.js' for the websocket endpoint on the server

* To run in development mode

  * on `sbt` run `backend/run`
  * on `sbt` run `frontend/fastLinkJS`
  * in `frontend` folder `npm run dev`

## added `sbt-revolver`

* Added the plug-in to sbt configuration

* Now to start the 'backend`:

  * on `sbt` run `backend/reStart` (or `~backend/reStart` to triggered restarting the app with changes)

  * you also have `reStop` and `reStatus`
