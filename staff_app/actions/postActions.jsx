export const addNewFacility = async (axios, facilityData) => {
  return axios.post("facility/admin/add", facilityData)
}