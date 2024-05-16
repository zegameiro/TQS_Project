export const getTest = async (axios) => {
  return axios.get("facility/test").then((res) => {
    return res.data;
  });
}

export const getAllFacilities = async (axios) => {
  return axios.get("facility/all").then((res) => {
    return res.data;
  });
}