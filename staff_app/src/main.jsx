import React from "react"
import ReactDOM from "react-dom/client"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import ErrorPage from "./error-page.jsx"
import "./index.css"
import AdminPage from "./routes/AdminPage.jsx"
import Login from "./routes/Login.jsx"
import WorkerPage from "./routes/WorkerPage.jsx"

const router = createBrowserRouter([
  {
    path: "/",
    element: <Login />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/admin",
    element: <AdminPage />,
  },
  {
    path: "/dashboard",
    element: <WorkerPage />,
  },
])

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
)
