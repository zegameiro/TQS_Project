import HomeCard from "@/components/homeCard";


export default function Home() {
  return (
    <main>
      <div className="img-home"></div>

      <div style={{ margin: '10vh', display: 'flex', justifyContent: 'center'}}>
        <HomeCard text="Make Reservation"/>
      </div>


    </main>
  );
}
