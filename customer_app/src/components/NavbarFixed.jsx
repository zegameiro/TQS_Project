import { LogoNoBackground } from "../images";

import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenuToggle,
  NavbarMenu,
  NavbarMenuItem,
  Link,
  Button,
  Avatar,
} from "@nextui-org/react";

import { useNavigate } from "react-router-dom";

const NavbarFixed = () => {
  const navigate = useNavigate();

  /* 
    const menuItems = [
      "Profile",
      "Dashboard",
      "Activity",
      "Analytics",
      "System",
      "Deployments",
      "My Settings",
      "Team Settings",
      "Help & Feedback",
      "Log Out",
    ];  
   */

  // get URL

  const url = window.location.pathname;


  return (
    <Navbar disableAnimation isBordered className="p-4 bg-primary" maxWidth="2xl" >
      <NavbarContent className="sm:hidden text-white" justify="start">
        <NavbarMenuToggle />
      </NavbarContent>

      <NavbarContent className="sm:hidden pr-3">
        <NavbarBrand>
          <p className="font-bold text-inherit text-white">Beauty Plaza</p>
        </NavbarBrand>
      </NavbarContent>

      <NavbarContent className="hidden sm:flex gap-4">
        <Avatar
          src={LogoNoBackground}
          className="w-40 h-30 text-lg bg-primary cursor-pointer hover:shadow-lg"
          onClick={() => navigate("/")}
        />
        <NavbarItem
          {...(url === "/" || url.startsWith("/reservation?")) && { isActive: true }}
        >
          <Link href="/"
            {...(url === "/" || url.startsWith("/reservation?")) ?
              { color: "warning" } :
              { className: "text-white" }}
          >
            Make Reservation
          </Link>
        </NavbarItem>
        <NavbarItem
          {...url === "/reservation-check" && { isActive: true }}
        >
          <Link href="/reservation-check"
            {...url === "/reservation-check" ?
              { color: "warning" } :
              { className: "text-white" }}
          >
            Check Reservation
          </Link>
        </NavbarItem>
      </NavbarContent>


      {/*
      <NavbarContent justify="end">
        <NavbarItem className="hidden lg:flex">
          <Link href="#" className="text-white">
            Login
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Button as={Link} color="warning" href="#" variant="flat">
            Sign Up
          </Button>
        </NavbarItem>
      </NavbarContent>

      <NavbarMenu>
        {menuItems.map((item, index) => (
          <NavbarMenuItem key={`${item}-${index}`}>
            <Link
              className="w-full"
              color={
                index === 2
                  ? "warning"
                  : index === menuItems.length - 1
                    ? "danger"
                    : "foreground"
              }
              href="#"
              size="lg"
            >
              {item}
            </Link>
          </NavbarMenuItem>
        ))}
      </NavbarMenu>
      */}
    </Navbar >
  );
};

export default NavbarFixed;
