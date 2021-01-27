package com.yizheng.job.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yizheng.job.entity.ExtractRequestBody;
import com.yizheng.job.entity.ExtractResponseItem;
import com.yizheng.job.entity.Extraction;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MonkeyLearnClient {
    private static final String EXTRACT_URL= "https://api.monkeylearn.com/v3/extractors/ex_YCya9nrn/extract/";
    private static final String AUTH_TOKEN = "27454a4f1e2f8a0eb73031458dcebb732ec7a9d0";

    public List<Set<String>> extract(List<String> articles) {
        ObjectMapper mapper = new ObjectMapper();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost request = new HttpPost(EXTRACT_URL);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Authorization", "Token " + AUTH_TOKEN);
        ExtractRequestBody body = new ExtractRequestBody(articles, 3);
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
        try {
            request.setEntity(new StringEntity(jsonBody));
        } catch (UnsupportedEncodingException e) {
            return Collections.emptyList();
        }

        ResponseHandler<List<Set<String>>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList();
            }
            ExtractResponseItem[] results = mapper.readValue(entity.getContent(), ExtractResponseItem[].class);
            List<Set<String>> keywordList = new ArrayList<>();
            for (ExtractResponseItem result : results) {
                Set<String> keywords = new HashSet<>();
                for (Extraction extraction : result.extractions) {
                    keywords.add(extraction.parsedValue);
                }
                keywordList.add(keywords);
            }
            return keywordList;
        };

        try {
            return httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {

        List<String> articles = Arrays.asList(
                "<p>Nisum is a leading global digital commerce firm headquartered in California, with services spanning digital strategy and transformation, insights and analytics, blockchain, business agility, " +
                        "and custom software development. Founded in 2000 with the customer-centric motto “Building Success Together®,” Nisum has grown to over 1,400 professionals across the United States, Chile, " //+
//                        "India, and Pakistan. A preferred advisor to leading Fortune 500 brands, Nisum enables clients to achieve direct business growth by building the advanced technology they need to reach end customers " +
//                        "in today’s world, with immersive and seamless experiences across digital and physical channels.</p> <p>We are currently looking for UI Technical Lead profiles with a strong technical " +
//                        "and handson experience  in React / Node JS with ability to lead and work with a team of developers.</p> <p>What You will Do:</p> <ul> <li>Competent in programming and debugging across " +
//                        "multiple modules and dealing with related external dependencies</li> <li>Provides story or business requirements breakdown planning and estimation, reviews, and evaluates the team's " +
//                        "estimates</li> <li>Accountable for complex stories or business requirements</li> <li>Identifies feature and module dependencies</li> <li>Proposes mitigation plans for identified risks " +
//                        "and problems</li> <li>Suggests process improvements and best practices</li> <li>Able to identify, evaluate and discuss alternative technologies and techniques</li> <li>Interacts with " +
//                        "team members, other IT teams, business users, individual contributors, managers</li> <li>Actively shares knowledge within domain and outside domain as well</li> <li>Mentors co-workers " +
//                        "within area of expertise, improves other's productivity</li> </ul> <p>What You Know:</p> <ul> <li>10 years and above overall years of" +
//                        " experience</li> <li>Expertise in React / NodeJS</li> <li>Experience in managing a team</li> <li>Able to understand the requirements from the business stakeholders " +
//                        "and work with remote location teams.</li> <li>Experience in Devops is a nice to have skill</li> <li>Should have experience with New Relic</li> </ul> <p>Education: " +
//                        "Bachelor’s degree in Computer Science, Information Systems, Engineering, Computer Applications, or related field</p> <p>Nisum is an Equal Opportunity Employer " +
//                        "and we are proud of our ongoing efforts to foster diversity and inclusion in the workplace.</p> "//,"<p><strong>Help us Build the Future of Money</strong></p> <p>Gemini Trust Company, LLC (Gemini) is a licensed digital asset exchange and custodian. We built the Gemini platform so customers can buy, sell, and store digital assets (e.g., Bitcoin, Ethereum, and Zcash) in a regulated, secure, and compliant manner.</p> <p>Digital assets and blockchain technology have the power to transform the world for good. This truth, along with our core values, form the bedrock of our company and culture. At Gemini, no job is too small and no project too big as we endeavor to build the future of money. We are a mission-driven, team-based, inclusive, and determined community of thought leaders who invest in each other and the long game. Join us in our mission!</p> <p>   <strong>The Department: Infrastructure</strong></p> <p>Gemini’s Infrastructure team are key experts in building and operating Gemini's world class cryptocurrency exchange systems and networks, using a combination of high-speed data center equipment and cloud-hosted components. With a focus on building, automating, and scaling Gemini’s product offerings, the Infrastructure keeps a close eye on the horizon and thinks steps ahead of the company’s business needs to collaboratively build and scale its growing footprint.</p> <p><strong>The Role: SRE</strong></p> <p>As a member of the team, you’ll have a hand in building innovative and robust solutions to stay ahead of our business growth. The ideal candidate is passionate about helping developers build the best products in the market, ensuring that they have the infrastructure to maintain our standards of high performance; they possess a strong sense of ownership and a desire to produce excellent results on the first try.</p> <p><strong>Responsibilities:</strong></p> <ul> <li>Utilize automation (via Ansible) to improve productivity, workflow, and technology deployment</li> <li>Build and support optimized network infrastructure</li> <li>Evaluate and implement new technologies</li> <li>Troubleshoot infrastructure and application performance issues, and find and improve performance bottlenecks</li> <li>Participate in the team's 24/7 on-call rotation</li> </ul> <p><strong>Requirements:</strong></p> <ul> <li>4+ years experience with Linux system administration</li> <li>2+ years experience with configuration management systems (Ansible, Puppet, Chef, Salt)</li> <li>2+ years experience with metrics collection and monitoring (open source or commercial)</li> <li>A keen sense of automation to create consistent and predictable infrastructure</li> <li>Working knowledge of industry best practices with regards to information security</li> <li>Understanding of IP network functionality (e.g., TCP, UDP, multicast)</li> <li>Working knowledge of how to debug networking issues</li> <li>Experience building and scaling distributed, highly available systems</li> <li>Familiarity with infrastructure management and operations lifecycle concepts and ecosystem</li> </ul> <p><strong>Preferred:</strong></p> <ul> <li>5+ years running bare-metal systems</li> <li>System tuning and kernel optimization</li> <li>2+ years experience in cloud platform administration </li> </ul> <p><strong>It Pays to Work Here</strong></p> <p>We take a holistic approach to compensation at Gemini, which includes:</p> <ul> <li>Competitive base salaries across all departments</li> <li>Ownership in the company via profit sharing units</li> <li>Amazing benefits, 401k match contribution, and flexible hours</li> <li>Snacks, Perks, Wellness Outings &amp; Events</li> </ul> <p>Gemini is proud to be an equal opportunity workplace and is an affirmative action employer. We are committed to equal employment opportunity regardless of race, color, ancestry, religion, sex, national origin, sexual orientation, age, citizenship, marital status, disability, gender identity, or Veteran status. If you have a disability or special need that requires accommodation, please let us know.</p> "
                //"<p>Protenus is paving the way in healthcare with a leading, comprehensive approach to compliance analytics. Providing healthcare leaders full insight into how health data is being used, and alerting privacy, security and compliance teams to inappropriate activity, Protenus helps our partner hospitals make decisions about how to better protect their data, their patients, and their institutions. This year, Protenus was named one of Forbes' Best Startup Employers, one of the Best Places to Work in Healthcare and Family Friendliest Companies by Modern Healthcare, and certified as a Great Place to Work.</p><p>We are rapidly expanding and looking to strengthen our front-end development team. We’re looking for talented individuals who are smart, passionate, and want to drive a positive change in the health IT industry. If you thrive in a fast-paced, collaborative workplace, Protenus provides an environment where you will be challenged and inspired every day. If you relish the freedom to bring creative, thoughtful solutions to the table that reflect your experience and personality, there's no limit to what you can accomplish here.To hear more from our technical team and learn about what we're building, check out our <a href=\"https://medium.com/protenus\">tech blog</a>.</p><p><strong>Responsibilities</strong></p><p>We look for software engineers to help craft our product offering by bringing their breadth of experience to the table. Experience with full-stack development is used to craft and manipulate data in document-based databases and build out services-support for UI features.</p><p>Our front-end team is responsible for delivering accurate and insightful information through a delightful user experience. In turn, our product offers an array of interesting problems to solve: scalability, optimization, and modularizing a data-driven UI. Projects include building React components, engineering rich visualizations in D3, designing and building world-class interactions, and architecting back-end solutions in Java for complex customer needs. You will be working as part of a small team of front-end engineers who are focused on building new features and improvements within our modern interface for our rapidly growing and highly diverse customer base.</p><p><strong>Key Qualifications, Skills, Competencies</strong></p><ul><li>At least 3 years professional experience developing back-end web applications, particularly in Java</li><li>At least 5 years professional experience with JavaScript and can demonstrate strong knowledge of the language</li><li>You have a critical, detail-oriented eye for design and interaction, and aren't afraid to make design decisions</li><li>You love to build high-quality code with efficiency and maintainability in mind</li><li>You are comfortable working with the command line, specifically tools such as Git, NPM, and Make</li><li>You enjoy working on a team with talented developers and designers</li></ul><p><strong>Nice to have skills/experience (or you can learn them with us):</strong></p><ul><li>A strong portfolio of work that you can share, such as a personal website, or a publicly visible product (e.g. a GitHub or Codepen account that demonstrates what you love to do)</li><li>Experience with React, webpack, D3, or similar tools</li><li>Experience writing front-end unit tests using Jest, Karma, Mocha, or similar</li><li>Experience with Java</li><li>Experience with MongoDB</li><li>Experience consuming REST APIs</li><li>Experience with E2E testing and associated tools such as Selenium, Protractor, or others</li><li>Experience working in an agile development process</li><li>Interest or experience in data collection and visualization</li></ul><p>We value diversity on our team and firmly believe Protenus is stronger when we hire people who make their own unique contributions to our culture. We welcome all applicants and encourage candidates from underrepresented backgrounds to apply. Join our team to see how you can learn and grow with us. </p>".replaceAll("\u00a0", " ")
//                "Elon Musk has shared a photo of the spacesuit designed by SpaceX. This is the second image shared of the new design and the first to feature the spacesuit’s full-body look.",
//                "Former Auburn University football coach Tommy Tuberville defeated ex-US Attorney General Jeff Sessions in Tuesday nights runoff for the Republican nomination for the U.S. Senate. ",
//                "The NEOWISE comet has been delighting skygazers around the world this month – with photographers turning their lenses upward and capturing it above landmarks across the Northern Hemisphere."
        );

        MonkeyLearnClient client = new MonkeyLearnClient();

        List<Set<String>> keywordList = client.extract(articles);
        System.out.println(keywordList);
    }
}
