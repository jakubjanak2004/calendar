import {useNavigate} from "react-router-dom";

export default function BackButton() {
    const navigate = useNavigate()

    function navigateBack() {
        navigate(-1)
    }

    return <>
        <button
            onClick={navigateBack}
            title="Go Back"
        >
            Back
        </button>
    </>
}