import { Avatar, Dropdown, Navbar } from "flowbite-react"

export default function NavbarComponent() {
  return (
    <Navbar fluid className="bg-[#1F0F53]">
      <Navbar.Brand href="logo-black-no-background.svg">
        <img
          src="/icon.svg"
          className="mr-3 h-6 sm:h-9"
          alt="BeautyPlaza Logo"
        />
      </Navbar.Brand>
      <div className="flex md:order-2">
        <Dropdown
          arrowIcon={false}
          inline
          label={
            <Avatar
              alt="User settings"
              img="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
              rounded
            />
          }
        >
          <Dropdown.Header>
            <span className="block text-sm">Luís Santos</span>
            <span className="block truncate text-sm font-medium">
              luissantos@beautyplaza.pt
            </span>
          </Dropdown.Header>
          <Dropdown.Divider />
          <Dropdown.Item>Sign out</Dropdown.Item>
        </Dropdown>
        <Navbar.Toggle />
      </div>
      <Navbar.Collapse>
        {/* TODO não usar href, apenas aqui para protótipo */}
        <Navbar.Link href="/dashboard" className="text-[#FFDB99]">
          Dashboard
        </Navbar.Link>
        <Navbar.Link href="/admin" className="text-[#FFDB99]">Admin Page</Navbar.Link>
      </Navbar.Collapse>
    </Navbar>
  )
}
