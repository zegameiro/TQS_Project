import { Button, Label, TextInput } from "flowbite-react"
import { useForm } from "react-hook-form"
import PropTypes from "prop-types"
import { FaChair } from "react-icons/fa"

ChairForm.propTypes = {
  roomID: PropTypes.number.isRequired,
  onSubmit: PropTypes.func.isRequired,
}

export default function ChairForm({ roomID, onSubmit }) {
  const { register, handleSubmit, reset } = useForm()

  const handleFormSubmit = (data) => {
    onSubmit(data)
    reset()
  }

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <div className="max-w-56">
        <div className="mb-2 block">
          <Label htmlFor={`name-${roomID}`} value="Chair name" />
        </div>
        <TextInput
          {...register("name")}
          id={`name-${roomID}`}
          icon={FaChair}
          placeholder="Name of the new chair"
        />
        <input type="hidden" {...register("roomID")} value={roomID} />
      </div>
      <Button className="mt-2 mb-8" type="submit">
        Add chair
      </Button>
    </form>
  )
}
