import NavbarComponent from "../components/Navbar"

import { Button, Popover } from "flowbite-react"

export default function WorkerPage() {
  return (
    <>
      <div>
        <NavbarComponent />
        <div className="pt-5">
          <Popover
            aria-labelledby="default-popover"
            content={
              <div className="w-64 text-sm text-gray-500 dark:text-gray-400">
                <div className="border-b border-gray-200 bg-gray-100 px-3 py-2 dark:border-gray-600 dark:bg-gray-700">
                  <h3
                    id="default-popover"
                    className="font-semibold text-gray-900 dark:text-white"
                  >Called next customer</h3>
                </div>
                <div className="px-3 py-2">
                  <p>The seat is waiting...</p>
                </div>
              </div>
            }
            arrow={false}
          >
            <Button>Call next customer!</Button>
          </Popover>
        </div>
      </div>
    </>
  )
}
