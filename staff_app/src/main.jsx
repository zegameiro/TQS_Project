import React from "react"
import ReactDOM from "react-dom/client"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import ErrorPage from "./error-page.jsx"
import "./index.css"
import AdminPage from "./routes/AdminPage.jsx"
import Login from "./routes/Login.jsx"
import WorkerPage from "./routes/WorkerPage.jsx"
import FacilityPage from "./routes/FacilityPage.jsx"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"

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
    path: "/admin/facility/:id",
    element: <FacilityPage />,
  },
  {
    path: "/dashboard",
    element: <WorkerPage />,
  },
])

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </React.StrictMode>
)
